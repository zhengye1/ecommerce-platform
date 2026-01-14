package com.ecommerce.order.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for address.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
