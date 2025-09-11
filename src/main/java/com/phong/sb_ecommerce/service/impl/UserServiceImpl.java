package com.phong.sb_ecommerce.service.impl;

import com.phong.sb_ecommerce.exception.APIException;
import com.phong.sb_ecommerce.exception.ResourcesAlreadyExistException;
import com.phong.sb_ecommerce.exception.ResourcesNotFoundException;
import com.phong.sb_ecommerce.mapper.UserMapper;
import com.phong.sb_ecommerce.model.ERole;
import com.phong.sb_ecommerce.model.Role;
import com.phong.sb_ecommerce.model.User;
import com.phong.sb_ecommerce.payload.request.SignupRequest;
import com.phong.sb_ecommerce.payload.request.UserUpdateRequestDTO;
import com.phong.sb_ecommerce.payload.response.ApiResponse;
import com.phong.sb_ecommerce.payload.response.UserInfoResponse;
import com.phong.sb_ecommerce.payload.response.UserResponseDTO;
import com.phong.sb_ecommerce.repository.RoleRepository;
import com.phong.sb_ecommerce.repository.UserRepository;
import com.phong.sb_ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserInfoResponse createUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ResourcesAlreadyExistException("User", "Username", signupRequest.getUsername());
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ResourcesAlreadyExistException("User", "Email", signupRequest.getEmail());
        }

        User newUser = User.builder()
            .username(signupRequest.getUsername())
            .password(passwordEncoder.encode(signupRequest.getPassword()))
            .email(signupRequest.getEmail())
            .build();

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                .orElseThrow(() -> new ResourcesNotFoundException("Role", "role name", ERole.ROLE_USER.toString()));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new ResourcesNotFoundException("Role", "Role", ERole.ROLE_ADMIN.toString()));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(ERole.ROLE_SELLER)
                            .orElseThrow(() -> new ResourcesNotFoundException("Role", "Role", ERole.ROLE_SELLER.toString()));
                        roles.add(sellerRole);
                        break;
                    default: // or throw an exception, example: RoleNotFoundException
                        Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                            .orElseThrow(() -> new ResourcesNotFoundException("Role", "Role", ERole.ROLE_USER.toString()));
                        roles.add(userRole);
                }
            });
        }

        newUser.setRoles(roles);

        User savedUser = userRepository.save(newUser);

        List<String> listRoles = new ArrayList<>();
        savedUser.getRoles().forEach(role -> {
            listRoles.add(role.getRoleName().name());
        });

        return new UserInfoResponse(savedUser.getUserId(), savedUser.getUsername(), savedUser.getEmail(), listRoles);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new APIException("User list is empty");
        }

        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        users.forEach(user -> {
            List<String> listRoles = new ArrayList<>();
            user.getRoles().forEach(role -> {
                listRoles.add(role.getRoleName().name());
            });
            userResponseDTOS.add(new UserResponseDTO(user.getUserId(), user.getUsername(), user.getEmail(), listRoles));
        });

        return userResponseDTOS;
    }

    @Override
    public ApiResponse<?> getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourcesNotFoundException("User", "id", id.toString()));

        List<String> listRoles = new ArrayList<>();
        user.getRoles().forEach(role -> {
            listRoles.add(role.getRoleName().name());
        });

        return ApiResponse.builder()
            .status("SUCCESS")
            .message("Select user with username: " + user.getUsername() + " successfully.")
            .response(new UserResponseDTO(user.getUserId(), user.getUsername(), user.getEmail(), listRoles))
            .build();
    }

    @Override
    public ApiResponse<?> deleteUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourcesNotFoundException("User", "id", id.toString()));

        List<String> listRoles = new ArrayList<>();
        user.getRoles().forEach(role -> {
            listRoles.add(role.getRoleName().name());
        });

        UserResponseDTO responseDTO = new UserResponseDTO(user.getUserId(), user.getUsername(), user.getEmail(), listRoles);

        userRepository.delete(user);
        return ApiResponse.builder()
            .status("SUCCESS")
            .message("User deleted")
            .response(responseDTO)
            .build();
    }

    @Override
    public ApiResponse<?> updateUser(Long userId, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourcesNotFoundException("User", "id", userId.toString()));

        user.setUsername(userUpdateRequestDTO.getUsername());
        user.setEmail(userUpdateRequestDTO.getEmail());

        Set<Role> roles = new HashSet<>();

        userUpdateRequestDTO.getRoles().forEach(role -> {
            switch (role) {
                case "admin":
                    Role roleAdmin = new Role(ERole.ROLE_ADMIN);
                    roles.add(roleAdmin);
                    break;
                case "seller":
                    Role roleSeller = new Role(ERole.ROLE_SELLER);
                    roles.add(roleSeller);
                    break;
                default:
                    Role userRole = new Role(ERole.ROLE_USER);
                    roles.add(userRole);
                    break;
            }
        });

        user.setRoles(roles);
        User updatedUser = userRepository.save(user);

        List<String> strRoles = new ArrayList<>();
        updatedUser.getRoles().forEach(role -> {
            strRoles.add(role.getRoleName().name());
        });

        UserResponseDTO updatedUserDTO = new UserResponseDTO(updatedUser.getUserId(), updatedUser.getUsername(), updatedUser.getEmail(), strRoles);
        return ApiResponse.builder()
            .status("SUCCESS")
            .message("User updated successfully")
            .response(updatedUserDTO)
            .build();
    }

}
