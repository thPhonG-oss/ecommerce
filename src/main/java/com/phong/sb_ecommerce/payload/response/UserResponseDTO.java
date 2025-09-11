package com.phong.sb_ecommerce.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phong.sb_ecommerce.model.Product;
import com.phong.sb_ecommerce.model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDTO {
    Long userId;
    String username;
    String email;
    List<String> roles = new ArrayList<>();
}
