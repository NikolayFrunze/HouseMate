package org.example.dto;

import java.math.BigDecimal;

/**
 * One expense sent to the client (Android).
 * sharePerPerson is the amount each member of the house has to pay:
 * amount divided by the number of members of the house.
 * Example: Light 80$ with 5 members -> sharePerPerson = 16.00
 */
public record ExpenseResponse(Long id, String name, BigDecimal amount, BigDecimal sharePerPerson) {
}
