package org.example.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data sent to the client (Android) with the expenses of the house of a user (GET /users/{userId}/expenses).
 * totalAmount is the sum of all the expenses and memberCount is how many users share them.
 */
public record ExpensesResponse(
        Long houseId,
        long memberCount,
        BigDecimal totalAmount,
        List<ExpenseResponse> expenses
) {
}
