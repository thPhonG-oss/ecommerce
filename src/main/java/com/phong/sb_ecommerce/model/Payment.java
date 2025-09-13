package com.phong.sb_ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long paymentId;

    @Column(name = "payment_method")
    @NotBlank
    String paymentMethod;

    @OneToOne(
        mappedBy = "payment",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    Order order;

    @Column(name = "pg_payment_id")
    String pgPaymentId; // pg- payment gateway

    @Column(name = "pg_status")
    String pgStatus;

    @Column(name = "pg_response_message")
    String pgResponseMessage;

    @Column(name = "pg_name")
    String pgName;

    // custom constructor
    public Payment(Long paymentId, String pgPaymentId, String pgStatus, String pgResponseMessage, String pgName) {
        this.paymentId = paymentId;
        this.pgPaymentId = pgPaymentId;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
        this.pgName = pgName;
    }
}
