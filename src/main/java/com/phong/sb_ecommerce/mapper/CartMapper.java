package com.phong.sb_ecommerce.mapper;

import com.phong.sb_ecommerce.model.Cart;
import com.phong.sb_ecommerce.payload.dto.CartDTO;
import org.aspectj.lang.annotation.After;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {
    @Mapping(target = "cartItems", source = "cartItems")
    @Mapping(target = "totalItems", expression = "java(cart.getCartItems() != null ? cart.getCartItems().size() : 0)")
    CartDTO toCartDTO(Cart cart);

}
