package com.phong.sb_ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    User cartUser;

    @OneToMany(
        fetch = FetchType.EAGER, // I want to load all cart items when fetching cart
        mappedBy = "cart",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
        orphanRemoval = true
    )
    List<CartItem> cartItems = new ArrayList<>();

    @Column(name = "total_price")
    Double totalPrice = 0.00;
}
