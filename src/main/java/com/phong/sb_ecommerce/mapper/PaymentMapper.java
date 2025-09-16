package com.phong.sb_ecommerce.mapper;

import com.phong.sb_ecommerce.model.Payment;
import com.phong.sb_ecommerce.payload.dto.PaymentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDTO toPaymentDTO(Payment payment);
}
