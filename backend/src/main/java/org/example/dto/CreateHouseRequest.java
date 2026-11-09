package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data received in POST /users/{userId}/house to register a house.
 * The annotations are checked automatically because the controller uses @Valid.
 */
public record CreateHouseRequest(
        // Street address: required, max 255 characters
        @NotBlank @Size(max = 255) String address,
        // Postal code: required, max 20 characters
        @NotBlank @Size(max = 20) String postalCode,
        // Housing code: required, max 50 characters
        @NotBlank @Size(max = 50) String housingCode,

        @NotBlank @Size(max = 6) String joinCode
) {
}
