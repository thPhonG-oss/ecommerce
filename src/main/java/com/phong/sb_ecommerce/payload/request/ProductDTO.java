package com.phong.sb_ecommerce.payload.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    Long productId;
    @NotEmpty
    @NotBlank(message = "Product name can not be blank.")
    @Size(min = 3, message = "Product name must contain at least 3 character.")
    String productName;
    String image;

    @NotBlank(message = "product's descripton can not be blank.")
    String description;


    Integer quantity;

    double price;

    double discount;
    double specialPrice;
}
