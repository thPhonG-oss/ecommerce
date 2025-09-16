package com.phong.sb_ecommerce.mapper;

import com.phong.sb_ecommerce.model.Order;
import com.phong.sb_ecommerce.model.OrderItem;
import com.phong.sb_ecommerce.payload.dto.OrderDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {
    OrderDTO toOrderDTO(Order order);
}
