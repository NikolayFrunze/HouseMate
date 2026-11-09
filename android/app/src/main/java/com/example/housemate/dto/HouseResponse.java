package com.example.housemate.dto;

/**
 * Data received from the backend with the house of a user (GET/POST /users/{userId}/house).
 * Field names must match the JSON keys sent by Spring Boot.
 */
public class HouseResponse {

    // Unique id of the house in the database
    public Long id;
    // Id of the user who owns the house (foreign key to users.id)
    public Long userId;
    // Street address of the house
    public String address;
    // Postal code of the house
    public String postalCode;
    // Housing code of the flat/house
    public String housingCode;

    public String joinCode;
}
