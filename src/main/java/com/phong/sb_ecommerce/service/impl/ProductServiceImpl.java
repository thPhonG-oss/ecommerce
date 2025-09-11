package com.phong.sb_ecommerce.service.impl;

import com.phong.sb_ecommerce.exception.APIException;
import com.phong.sb_ecommerce.exception.ResourcesAlreadyExistException;
import com.phong.sb_ecommerce.exception.ResourcesNotFoundException;
import com.phong.sb_ecommerce.mapper.ProductMapper;
import com.phong.sb_ecommerce.model.Category;
import com.phong.sb_ecommerce.model.Product;
import com.phong.sb_ecommerce.payload.request.ProductDTO;
import com.phong.sb_ecommerce.payload.response.ProductResponse;
import com.phong.sb_ecommerce.repository.CategoryRepository;
import com.phong.sb_ecommerce.repository.ProductRepository;
import com.phong.sb_ecommerce.service.FileService;
import com.phong.sb_ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private FileService fileService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Value("${project.images}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourcesNotFoundException("Category", "Category Id", categoryId));

//        if(productRepository.findByProductName(productDTO.getProductName()) != null){
//            throw new ResourcesAlreadyExistException("Product", "product name", productDTO.getProductName());
//        }

        Set<Product> products = category.getProducts();
        products.forEach(product -> {
            if(product.getProductName().equals(productDTO.getProductName()))
                throw new ResourcesAlreadyExistException("Product", "product name", productDTO.getProductName());
        });

        Product product = productMapper.toProduct(productDTO);

        product.setImage("default.png");
        double specialPrice = (100 - productDTO.getDiscount()) * 0.01 * product.getPrice();
        product.setSpecialPrice(specialPrice);
        product.setCategory(category);
        Product savedProduct =  productRepository.save(product);
        return productMapper.toProductDTO(savedProduct);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("ASC")?Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<Product> products = productPage.getContent();
        if(products.isEmpty()){
            throw new APIException("No Product created until now.");
        }

        List<ProductDTO> productDTOS = new ArrayList<>();
        products.forEach(product -> {
            productDTOS.add(productMapper.toProductDTO(product));
        });

        return new ProductResponse(
                productDTOS,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("ASC")?Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourcesNotFoundException("Category", "category Id", categoryId));

        Page<Product> productPage = productRepository.findAllByCategory(category, pageable);

        List<Product> productList = productPage.getContent();

        if(productList.isEmpty())
            throw new APIException("No product created until now.");

        List<ProductDTO> productDTOS = new ArrayList<>();
        productList.forEach(product -> {
            productDTOS.add(productMapper.toProductDTO(product));
        });

        return new ProductResponse(
                productDTOS,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("ASC")?Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);

//        List<Product> products = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%");

        Page<Product> productPage = productRepository.findAllByProductNameLikeIgnoreCase("%" + keyword + "%", pageable);


        List<Product> products = productPage.getContent();

        if(products.isEmpty()){
            throw new ResourcesNotFoundException("Product", "product name", keyword);
        }

        List<ProductDTO> productDTOS = new ArrayList<>();
        products.forEach(product -> {
            productDTOS.add(productMapper.toProductDTO(product));
        });

        return new ProductResponse(
                productDTOS,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourcesNotFoundException("Product", "product id", productId));

        existingProduct.setProductName(productDTO.getProductName());
//        existingProduct.setImage(existingProduct.getImage());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDiscount(productDTO.getDiscount());
        existingProduct.setQuantity(productDTO.getQuantity());

        existingProduct.setSpecialPrice((100 - productDTO.getDiscount()) * 0.01 * productDTO.getPrice());

        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toProductDTO(updatedProduct);
    }

    @Override
    public String deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourcesNotFoundException("Product", "product id", productId));

        productRepository.delete(existingProduct);
        return "Delete successfully!";
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourcesNotFoundException("Product", "product id", productId));

//        String path = "images/";
        String fileName = fileService.uploadImage(path, image);

        product.setImage(fileName);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toProductDTO(updatedProduct);
    }


}
