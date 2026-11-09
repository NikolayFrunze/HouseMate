package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Data received in POST /users/{userId}/expenses to add an expense to the house of the user.
 * The annotations are checked automatically because the controller uses @Valid.
 */
public record CreateExpenseRequest(
        // Name of the expense: required, max 100 characters
        @NotBlank @Size(max = 100) String name,
        // Amount: required, at least 0.01, max 10 integer digits and 2 decimals
        @NotNull @DecimalMin("0.01") @Digits(integer = 10, fraction = 2) BigDecimal amount
) {
}
