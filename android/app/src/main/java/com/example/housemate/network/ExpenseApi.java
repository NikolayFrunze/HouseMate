package com.example.housemate.network;

import com.example.housemate.dto.ExpensesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Describes the HTTP endpoints of the backend related to expenses.
 * Retrofit generates the code that actually sends these requests.
 */
public interface ExpenseApi {

    /**
     * Gets the expenses of the house of a user: GET {BASE_URL}users/{userId}/expenses
     * Answers 200 with the expenses, or 404 if the user does not belong to any house.
     */
    @GET("users/{userId}/expenses")
    Call<ExpensesResponse> getExpenses(@Path("userId") long userId);
}
