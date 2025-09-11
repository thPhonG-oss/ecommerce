package com.phong.sb_ecommerce.payload.response;

import com.phong.sb_ecommerce.model.Product;
import com.phong.sb_ecommerce.payload.request.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {
    private List<ProductDTO> productDTOS;
    Integer pageNumber;
    Integer pageSize;
    Long totalElements;
    Integer totalPages;
    Boolean lastPage;

    public ProductResponse(List<ProductDTO> productDTOS){
        this.productDTOS = productDTOS;
    }
}
