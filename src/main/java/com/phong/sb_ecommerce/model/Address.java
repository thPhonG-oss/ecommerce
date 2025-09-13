package com.phong.sb_ecommerce.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    Long addressId;

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
    @Size(min = 4, message = "Pincode must be at least 4 character.")
    String pincode;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(buildingName, address.buildingName) && Objects.equals(street, address.street) && Objects.equals(city, address.city) && Objects.equals(country, address.country) && Objects.equals(pincode, address.pincode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buildingName, street, city, country, pincode);
    }
}
