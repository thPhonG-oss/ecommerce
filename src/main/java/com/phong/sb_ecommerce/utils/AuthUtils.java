package com.phong.sb_ecommerce.utils;

import com.phong.sb_ecommerce.exception.ResourcesNotFoundException;
import com.phong.sb_ecommerce.model.User;
import com.phong.sb_ecommerce.payload.response.UserResponseDTO;
import com.phong.sb_ecommerce.repository.UserRepository;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    @Autowired
    private UserRepository userRepository;


    public String loggedInEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return null;
        }

        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new ResourcesNotFoundException("User", "username", auth.getName()));

        return user.getEmail();
    }

    public User loggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return null;
        }
        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new ResourcesNotFoundException("User", "username", auth.getName()));

        return user;
    }

    public Long loggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return null;
        }
        User user = userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new ResourcesNotFoundException("User", "username", auth.getName()));

        return user.getUserId();
    }
}
