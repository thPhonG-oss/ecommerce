package com.phong.sb_ecommerce.service;

import com.phong.sb_ecommerce.payload.request.LoginRequest;
import com.phong.sb_ecommerce.payload.request.SignupRequest;
import com.phong.sb_ecommerce.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse<?>> authenticate(LoginRequest loginRequest);

    ApiResponse<?> registerUser(SignupRequest signupRequest);

    ResponseEntity<?> signoutUser();
}
