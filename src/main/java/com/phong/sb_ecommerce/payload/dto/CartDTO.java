package com.phong.sb_ecommerce.payload.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phong.sb_ecommerce.model.CartItem;
import jakarta.persistence.JoinColumn;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDTO {
    Long cartId;
    Double totalPrice=0.00;

    List<CartItemDTO> cartItems= new ArrayList<>();
    Integer totalItems = 0;
}
