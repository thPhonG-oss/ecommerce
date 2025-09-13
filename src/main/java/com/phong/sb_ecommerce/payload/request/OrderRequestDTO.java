package com.phong.sb_ecommerce.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestDTO {
    Long addressId;
    String paymentMethod;
    String pgName;
    String pgPaymentId;
    String pgStatus;
    String pgResponseMessage;
}
