package com.phong.sb_ecommerce.mapper;

import com.phong.sb_ecommerce.model.OrderItem;
import com.phong.sb_ecommerce.payload.dto.OrderItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
}
