package com.example.housemate.dto;

import java.math.BigDecimal;

/**
 * One expense received from the backend (inside ExpensesResponse).
 * Field names must match the JSON keys sent by Spring Boot.
 */
public class ExpenseResponse {

    // Unique id of the expense in the database
    public Long id;
    // Name of the expense, for example "Luz"
    public String name;
    // Total amount of the expense
    public BigDecimal amount;
    // Amount each member of the house has to pay (amount / number of members)
    public BigDecimal sharePerPerson;
}
