package com.phong.sb_ecommerce.controller;

import com.phong.sb_ecommerce.payload.request.ProductDTO;
import com.phong.sb_ecommerce.payload.response.ProductResponse;
import com.phong.sb_ecommerce.repository.ProductRepository;
import com.phong.sb_ecommerce.service.ProductService;
import com.phong.sb_ecommerce.utils.AppConstant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestBody @Valid ProductDTO productDTO
    ){
        return new ResponseEntity<>(productService.addProduct(categoryId, productDTO), HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", required = false,defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.SORT_PRODUCT_BY) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstant.SORT_DIR) String sortOrder
    ){
        return new ResponseEntity<>(
                productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder),
                HttpStatus.OK
        );
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestParam(name = "pageNumber", required = false,defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.SORT_PRODUCT_BY) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstant.SORT_DIR) String sortOrder
    ){

        return new ResponseEntity<>(productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }


    @GetMapping("public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable(name = "keyword") String keyword,
            @RequestParam(name = "pageNumber", required = false,defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstant.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstant.SORT_PRODUCT_BY) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstant.SORT_DIR) String sortOrder
    ){
        return new ResponseEntity<>(productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }


    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable(name = "productId") Long productId,
            @RequestBody @Valid ProductDTO productDTO
    ){
        return new ResponseEntity<>(productService.updateProduct(productId,productDTO), HttpStatus.OK);
    }
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "productId") Long productId){
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable(name = "productId") Long productId,
            @RequestBody MultipartFile image
            ) throws IOException {
        return new ResponseEntity<>(productService.updateProductImage(productId, image), HttpStatus.OK);
    }

}
