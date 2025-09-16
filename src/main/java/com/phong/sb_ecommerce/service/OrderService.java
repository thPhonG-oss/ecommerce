package com.phong.sb_ecommerce.service;

import com.phong.sb_ecommerce.payload.dto.OrderDTO;
import jakarta.transaction.Transactional;

public interface OrderService {

    @Transactional
    OrderDTO placeOrder(Long addressId, String paymentMethod, String pgPaymentId, String pgPaymentStatus, String pgName, String pgResponseMessage);
}
