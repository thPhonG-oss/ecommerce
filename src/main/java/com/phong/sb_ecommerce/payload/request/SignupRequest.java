package com.phong.sb_ecommerce.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest {
    @NotBlank(message = "username must be not blank.")
    @Size(min = 2, max = 50)
    String username;

    @NotBlank
    String password;


    @NotBlank
    @Size(max = 120)
    @Email
    String email;
    Set<String> roles;
}
