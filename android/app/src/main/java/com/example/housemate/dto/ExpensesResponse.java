package com.example.housemate.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data received from the backend with the expenses of the house of a user
 * (GET /users/{userId}/expenses). Field names must match the JSON keys sent by Spring Boot.
 */
public class ExpensesResponse {

    // Id of the house the expenses belong to
    public Long houseId;
    // How many users share the expenses of the house
    public long memberCount;
    // Sum of all the expenses
    public BigDecimal totalAmount;
    // List of expenses (can be empty)
    public List<ExpenseResponse> expenses;
}
