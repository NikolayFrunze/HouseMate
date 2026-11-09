package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.CreateExpenseRequest;
import org.example.dto.ExpenseResponse;
import org.example.dto.ExpensesResponse;
import org.example.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for the expenses of the house of a user.
 * Endpoints used by the Android app (ExpenseApi.java):
 *   GET  /users/{userId}/expenses -> 200 with the expenses of the house,
 *                                    or 404 if the user does not belong to a house
 *   POST /users/{userId}/expenses -> 201 with the saved expense, or 404 if the user has no house
 */
@RestController
@RequestMapping("/users/{userId}/expenses")
public class ExpenseController {

    // Business logic used by the endpoints
    private final ExpenseService expenseService;

    // Spring injects the service automatically
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * GET /users/{userId}/expenses
     * Returns the expenses of the house of the user and what each member has to pay.
     */
    @GetMapping
    public ResponseEntity<ExpensesResponse> getExpenses(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getExpenses(userId));
    }

    /**
     * POST /users/{userId}/expenses
     * Adds an expense to the house of the user. Returns 201 (Created) with the saved expense.
     */
    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(@PathVariable Long userId,
                                                      @Valid @RequestBody CreateExpenseRequest request) {
        ExpenseResponse created = expenseService.addExpense(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Handles the error when the user does not belong to any house.
     * Returns 404 (Not Found) with the error message as text.
     */
    @ExceptionHandler(ExpenseService.NotInHouseException.class)
    public ResponseEntity<String> handleNotInHouse(ExpenseService.NotInHouseException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
