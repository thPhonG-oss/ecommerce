package com.phong.sb_ecommerce.payload.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDTO {

    Long paymentId;

    String pgPaymentId; // pg- payment gateway

    String pgStatus;

    String pgResponseMessage;

    String pgName;
}
