package com.phong.sb_ecommerce.security;

import com.phong.sb_ecommerce.model.ERole;
import com.phong.sb_ecommerce.model.Role;
import com.phong.sb_ecommerce.model.User;
import com.phong.sb_ecommerce.repository.RoleRepository;
import com.phong.sb_ecommerce.repository.UserRepository;
import com.phong.sb_ecommerce.security.jwt.AuthTokenFilter;
import com.phong.sb_ecommerce.security.jwt.AuthentryPointJwt;
import com.phong.sb_ecommerce.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final String[] AUTH_WHITELIST = {"/api/public/**",
        "/v3/api-docs",
        "/swagger-resources/**",
        "/webjars/**",
        "/swagger-ui/**",
        "/h2-console/**",
        "/api/auth/**",
        "/api/test/**",
        "/favicon.ico",
        "/error/**",
    };


    @Autowired
    private AuthentryPointJwt authentryPointJwt;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> {
                exception.authenticationEntryPoint(authentryPointJwt);
            })
            .sessionManagement(sessionManagement -> {
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .authorizeHttpRequests(authorizeRequests -> {
                authorizeRequests.requestMatchers(AUTH_WHITELIST).permitAll()
                    .anyRequest().authenticated();
            });

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(header ->
            header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean // it completely bypass from spring security
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (webSecurity -> {
            webSecurity.ignoring().requestMatchers(
                "/v3/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/h2-console/**",
                "/favicon.ico"
            );
        });
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                .orElseGet(() -> {
                    Role newUserRole = new Role(ERole.ROLE_USER);
                    return roleRepository.save(newUserRole);
                });

            Role sellerRole = roleRepository.findByRoleName(ERole.ROLE_SELLER)
                .orElseGet(() -> {
                    Role newSellerRole = new Role(ERole.ROLE_SELLER);
                    return roleRepository.save(newSellerRole);
                });

            Role adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                .orElseGet(() -> {
                    Role newAdminRole = new Role(ERole.ROLE_ADMIN);
                    return roleRepository.save(newAdminRole);
                });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);


            // Create users if not already present
            if (!userRepository.existsByUsername("user1")) {
                User user1 = User.builder()
                    .username("user1")
                    .email("user1@gmail.com")
                    .password(passwordEncoder.encode("password1"))
                    .build();
//                    new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUsername("seller1")) {
                User seller1 = User.builder()
                    .username("seller1")
                    .email("seller1@gmail.com")
                    .password(passwordEncoder.encode("password2"))
                    .build();
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUsername("admin")) {
                User admin = User.builder()
                    .username("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("password3"))
                    .build();
                userRepository.save(admin);
            }

            if (!userRepository.existsByUsername("testuser")) {
                User testUser = User.builder()
                    .username("testuser")
                    .email("testuser@gmail.com")
                    .password(passwordEncoder.encode("testpass"))
                    .build();
                userRepository.save(testUser);
            }

            // Update roles for existing users
            userRepository.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });

            userRepository.findByUsername("testuser").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });
        };
    }


}
