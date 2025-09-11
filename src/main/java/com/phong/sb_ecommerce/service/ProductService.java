package com.phong.sb_ecommerce.service;

import com.phong.sb_ecommerce.payload.request.ProductDTO;
import com.phong.sb_ecommerce.payload.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface ProductService {
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO);
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder );

    public ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    public String deleteProduct(Long productId);

    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

}
