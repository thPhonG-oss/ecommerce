package com.phong.sb_ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderId;

    @Email
    @Column(nullable = false)
    String email;

    @OneToMany(
        mappedBy = "order",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "order_date")
    LocalDate orderDate;

    @Column(name = "total_amount")
    Double totalAmount;

    @Column(name = "order_status")
    String orderStatus;

    @ManyToOne
    @JoinColumn(name = "address_id")
    Address address;

    @OneToOne
    @JoinColumn(name = "payment_id")
    Payment payment;
}
