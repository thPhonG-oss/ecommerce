package com.phong.sb_ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    int addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters.")
    @Column(name = "building_name")
    String buildingName;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters.")
    String street;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 characters.")
    String city;

    @NotBlank(message = "Country name must be not blank.")
    String country;

    @NotBlank
    @Size(min = 6, message = "Pincode must be at least 6 character.")
    String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    Set<User> users = new HashSet<>();
}
