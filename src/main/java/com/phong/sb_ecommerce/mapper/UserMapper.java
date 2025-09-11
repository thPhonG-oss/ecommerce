package com.phong.sb_ecommerce.mapper;

import com.phong.sb_ecommerce.model.User;
import com.phong.sb_ecommerce.payload.request.SignupRequest;
import com.phong.sb_ecommerce.payload.response.UserInfoResponse;
import com.phong.sb_ecommerce.payload.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(SignupRequest signupRequest);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "currentToken", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserInfoResponse toUserInfoResponse(User user);

    @Mapping(target = "roles", ignore = true)
    UserResponseDTO toUserResponseDTO(User user);

    @Mapping(target = "roles", ignore = true)
    User toUser(UserResponseDTO userResponseDTO);
}
