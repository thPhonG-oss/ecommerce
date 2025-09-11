package com.phong.sb_ecommerce.payload.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phong.sb_ecommerce.payload.request.ProductDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDTO {
    Long cartItemId;

    CartDTO cartDTO;

    ProductDTO productDTO;

    Integer quantity;
    double discount;
    double productPrice;
}
