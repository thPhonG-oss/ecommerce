package com.phong.sb_ecommerce.controller;

import com.phong.sb_ecommerce.payload.dto.OrderDTO;
import com.phong.sb_ecommerce.payload.request.OrderRequestDTO;
import com.phong.sb_ecommerce.repository.OrderRepository;
import com.phong.sb_ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(
        @PathVariable String paymentMethod,
        @RequestBody OrderRequestDTO orderRequestDTO
    ){
        return new ResponseEntity<OrderDTO>(orderService.placeOrder(orderRequestDTO.getAddressId(), paymentMethod, orderRequestDTO.getPgPaymentId(), orderRequestDTO.getPgStatus(), orderRequestDTO.getPgName(), orderRequestDTO.getPgResponseMessage()), HttpStatus.CREATED);
    }
}
