package com.phong.sb_ecommerce.service;

import com.phong.sb_ecommerce.payload.request.SignupRequest;
import com.phong.sb_ecommerce.payload.request.UserUpdateRequestDTO;
import com.phong.sb_ecommerce.payload.response.ApiResponse;
import com.phong.sb_ecommerce.payload.response.UserInfoResponse;
import com.phong.sb_ecommerce.payload.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserInfoResponse createUser(SignupRequest signupRequest);

    List<UserResponseDTO> getAllUsers();

    ApiResponse<?> getUserById(Long id);

    ApiResponse<?> deleteUserById(Long id);


    ApiResponse<?> updateUser(Long userId, UserUpdateRequestDTO userUpdateRequestDTO);
}
