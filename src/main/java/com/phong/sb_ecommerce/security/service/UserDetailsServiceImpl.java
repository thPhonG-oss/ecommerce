package com.phong.sb_ecommerce.security.service;

import com.phong.sb_ecommerce.model.User;
import com.phong.sb_ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // this ensures the database operations is handled in a transaction
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("Attempting to load user: {}", username);

        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

//        System.out.println(user.toString());
//        System.out.println(user.getPassword());
        logger.debug("Found user: {}", username);
        logger.debug("Stored password hash: {}", user.getPassword());
        logger.debug("User roles: {}", user.getRoles());
        return UserDetailsImpl.build(user);
    }
}
