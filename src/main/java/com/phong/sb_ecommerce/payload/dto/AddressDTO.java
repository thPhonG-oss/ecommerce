package com.phong.sb_ecommerce.payload.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressDTO {

    Integer addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters.")
    String buildingName;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters.")
    String street;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters.")
    String city;

    @NotBlank(message = "Country name must be not blank.")
    String country;

    @NotBlank
    @Size(min = 4, message = "Pincode must be at least 6 character.")
    String pincode;
}
