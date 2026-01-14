package com.ecommerce.order.domain.model;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Embeddable address value object.
 */
@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public String getFullAddress() {
        return String.format("%s, %s, %s %s, %s", street, city, state, zipCode, country);
    }
}
