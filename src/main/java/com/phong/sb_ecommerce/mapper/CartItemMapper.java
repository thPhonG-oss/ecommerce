package com.phong.sb_ecommerce.mapper;

import com.phong.sb_ecommerce.model.CartItem;
import com.phong.sb_ecommerce.payload.dto.CartItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.TargetPropertyName;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartItemMapper {
    @Mapping(target = "productDTO", source = "product")
    @Mapping(target = "cartDTO", ignore = true)
    CartItemDTO toCartItemDTO(CartItem cartItem);
}
