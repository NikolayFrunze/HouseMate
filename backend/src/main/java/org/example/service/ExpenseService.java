package org.example.service;

import org.example.dto.CreateExpenseRequest;
import org.example.dto.ExpenseResponse;
import org.example.dto.ExpensesResponse;
import org.example.entity.Expense;
import org.example.entity.House;
import org.example.entity.HouseMember;
import org.example.repository.ExpenseRepository;
import org.example.repository.HouseMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Business logic for expenses.
 * Rule: a user can only see or add expenses of the house he/she belongs to.
 * The amount of each expense is split equally between all the members of that house.
 */
@Service
public class ExpenseService {

    // Used to read and write expenses in the database
    private final ExpenseRepository expenseRepository;
    // Used to find the house of a user and count its members
    private final HouseMemberRepository houseMemberRepository;

    // Spring injects both dependencies automatically through the constructor
    public ExpenseService(ExpenseRepository expenseRepository,
                          HouseMemberRepository houseMemberRepository) {
        this.expenseRepository = expenseRepository;
        this.houseMemberRepository = houseMemberRepository;
    }

    /**
     * Returns the expenses of the house of the user, with the amount each member has to pay.
     * Throws NotInHouseException if the user does not belong to any house.
     */
    @Transactional(readOnly = true)
    public ExpensesResponse getExpenses(Long userId) {
        // Step 1: the user must belong to a house
        House house = findHouseOfUser(userId);

        // Step 2: how many people share the expenses of that house
        long memberCount = houseMemberRepository.countByHouseId(house.getId());

        // Step 3: convert every expense and add up the total
        List<ExpenseResponse> expenses = expenseRepository.findByHouseIdOrderByIdAsc(house.getId())
                .stream()
                .map(expense -> toResponse(expense, memberCount))
                .toList();

        BigDecimal total = expenses.stream()
                .map(ExpenseResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExpensesResponse(house.getId(), memberCount, total, expenses);
    }

    /**
     * Adds an expense to the house of the user.
     * Throws NotInHouseException if the user does not belong to any house.
     */
    @Transactional
    public ExpenseResponse addExpense(Long userId, CreateExpenseRequest request) {
        House house = findHouseOfUser(userId);

        Expense expense = new Expense(
                house,
                request.name().trim(),
                request.amount().setScale(2, RoundingMode.HALF_UP)
        );
        Expense saved = expenseRepository.save(expense);

        long memberCount = houseMemberRepository.countByHouseId(house.getId());
        return toResponse(saved, memberCount);
    }

    // Returns the house of the user, or throws if the user is not a member of any house
    private House findHouseOfUser(Long userId) {
        return houseMemberRepository.findByUserId(userId)
                .map(HouseMember::getHouse)
                .orElseThrow(() -> new NotInHouseException(userId));
    }

    // Converts an expense into the data sent to the client, splitting it between the members
    private ExpenseResponse toResponse(Expense expense, long memberCount) {
        // memberCount is never 0 here: the user asking for the expense is a member
        BigDecimal share = expense.getAmount()
                .divide(BigDecimal.valueOf(memberCount), 2, RoundingMode.HALF_UP);

        return new ExpenseResponse(expense.getId(), expense.getName(), expense.getAmount(), share);
    }

    /**
     * Error thrown when the user does not belong to any house.
     * The controller turns it into HTTP 404 (Not Found).
     */
    public static class NotInHouseException extends RuntimeException {

        public NotInHouseException(Long userId) {
            super("User " + userId + " does not belong to any house");
        }
    }
}
