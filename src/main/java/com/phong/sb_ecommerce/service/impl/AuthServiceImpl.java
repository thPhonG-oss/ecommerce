package com.phong.sb_ecommerce.service.impl;

import com.phong.sb_ecommerce.payload.request.LoginRequest;
import com.phong.sb_ecommerce.payload.request.SignupRequest;
import com.phong.sb_ecommerce.payload.response.ApiResponse;
import com.phong.sb_ecommerce.payload.response.UserInfoResponse;
import com.phong.sb_ecommerce.security.jwt.JwtUtils;
import com.phong.sb_ecommerce.security.service.UserDetailsImpl;
import com.phong.sb_ecommerce.service.AuthService;
import com.phong.sb_ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


    @Override
    public ResponseEntity<ApiResponse<?>> authenticate(LoginRequest loginRequest) {
        logger.error("Authentication failed for user: {}", loginRequest.getUsername());

        Authentication authentication;
        try {
            authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            logger.error("Authentication failed for user: {}", loginRequest.getUsername());
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<>(ApiResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .message("Bad credentials")
                .build(), HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookies(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
            .id(userDetails.getId())
            .username(userDetails.getUsername())
            .email(userDetails.getEmail())
            .roles(roles)
            .currentToken(jwtCookie.getValue())
            .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .body(ApiResponse.builder()
                .message("Login Successful.")
                .status(HttpStatus.OK.toString())
                .response(userInfoResponse)
                .build());
    }

    @Override
    public ApiResponse<?> registerUser(SignupRequest signupRequest) {
        UserInfoResponse savedUserInfoResponse = userService.createUser(signupRequest);

        return ApiResponse.builder()
            .message("User registered successfully")
            .status(HttpStatus.CREATED.toString())
            .response(savedUserInfoResponse)
            .build();
    }

    @Override
    public ResponseEntity<?> signoutUser(){
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .body(ApiResponse.builder()
                .status(HttpStatus.OK.toString())
                .message("Signed out successfully")
            .build() );
    }

}
