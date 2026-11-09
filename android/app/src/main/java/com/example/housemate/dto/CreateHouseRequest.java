package com.example.housemate.dto;

/**
 * Data sent to the backend to register the house of a user (POST /users/{userId}/house).
 * Gson converts it to JSON: {"address": "...", "postalCode": "...", "housingCode": "..."}
 */
public class CreateHouseRequest {

    // Street address typed by the user
    public final String address;
    // Postal code typed by the user
    public final String postalCode;
    // Housing code typed by the user
    public final String housingCode;

    public CreateHouseRequest(String address, String postalCode, String housingCode) {
        this.address = address;
        this.postalCode = postalCode;
        this.housingCode = housingCode;
    }
}
