package com.phong.sb_ecommerce.repository;

import com.phong.sb_ecommerce.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@NotBlank(message = "username must be not blank.") @Size(max = 50) String username);

    boolean existsByUsername(@NotBlank(message = "username must be not blank.") @Size(max = 50) String username);

    boolean existsByEmail(@NotBlank @Size(max = 120) @Email String email);


}
