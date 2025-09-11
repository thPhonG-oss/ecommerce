package com.phong.sb_ecommerce.controller;

import com.phong.sb_ecommerce.payload.request.LoginRequest;
import com.phong.sb_ecommerce.payload.request.SignupRequest;
import com.phong.sb_ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signin(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }

    @PostMapping("/sign-up") // register user
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest signupRequest){
        return new ResponseEntity<>(authService.registerUser(signupRequest), HttpStatus.CREATED);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signout(){
        return authService.signoutUser();
    }
}
