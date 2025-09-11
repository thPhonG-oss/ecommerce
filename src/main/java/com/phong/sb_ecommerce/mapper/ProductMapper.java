package com.phong.sb_ecommerce.mapper;

import com.phong.sb_ecommerce.model.Product;
import com.phong.sb_ecommerce.payload.request.ProductDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toProductDTO(Product product);
    Product toProduct(ProductDTO productDTO);
}
