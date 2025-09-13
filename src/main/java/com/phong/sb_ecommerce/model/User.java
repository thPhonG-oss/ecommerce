package com.phong.sb_ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @NotBlank(message = "username must be not blank.")
    @Size(max = 50)
    String username;

    @NotBlank
    @Column(name = "password", nullable = false, length = 255)
    String password;
    String email;

    @ManyToMany(fetch = FetchType.EAGER
            , cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "user"
            , cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    List<Address> addresses = new ArrayList<>();

    @ToString.Exclude
    @OneToOne(
        mappedBy = "cartUser",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true
    )
    Cart cart;

    @ToString.Exclude // tránh infinite recursion trong mối quan hệ hai chiều. neeus hai phía cùng gọi toString của nhau thì sẽ dẫn tới lỗi StackOverflowError.
    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true // remove a orphan(mồ côi) record, it's mean remove a record when it wasn't no longer linked with a parent entity
    )
    Set<Product> products = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", username='" + username + '\'' +
            ", userId=" + userId +
            ", roles=" + roles +
            '}';
    }
}
