package com.phong.sb_ecommerce.payload.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    Long orderId;
    String email;
    List<OrderItemDTO> orderItems = new ArrayList<>();
    LocalDate orderDate;
    PaymentDTO payment;
    Double tolalAmount;
    String orderStatus;
    Long addressId;
}
