package org.example.dto;

/**
 * Data sent back to the client (Android) with the house of a user.
 * The field names match the keys that the Android app reads (HouseResponse.java).
 */
public record HouseResponse(Long id, Long userId, String address, String postalCode, String housingCode, String joinCode) {
}
