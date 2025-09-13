package com.phong.sb_ecommerce.payload.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phong.sb_ecommerce.model.Order;
import com.phong.sb_ecommerce.model.Product;
import com.phong.sb_ecommerce.payload.request.ProductDTO;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDTO {
    Long orderItemId;

    ProductDTO product;

    Integer quantity;

    Double discount;

    Double orderedProductPrice;
}
