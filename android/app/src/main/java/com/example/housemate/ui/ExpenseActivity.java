package com.example.housemate.ui;

import com.example.housemate.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housemate.dto.ExpenseResponse;
import com.example.housemate.dto.ExpensesResponse;
import com.example.housemate.network.ApiClient;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Screen opened from the "Expenses" button of {@link UserActivity}.
 * - If the user belongs to a house, the expenses of that house are shown, together with
 *   the amount each member has to pay (the backend does the split).
 * - If the user does not belong to any house, a message is shown instead.
 */
public class ExpenseActivity extends AppCompatActivity {

    private static final String TAG = "ExpenseActivity";

    // Id of the logged user, received from UserActivity
    private long userId;

    // Message shown at the top ("Loading...", "You don't belong to any house", errors...)
    private TextView textMessage;
    // Card with the summary and the expenses (shown when there is data)
    private View cardExpenses;
    // Text with the total and the number of members
    private TextView textSummary;
    // Container where one row per expense is added
    private LinearLayout layoutExpenseItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_activity);

        // Read the user id sent by UserActivity (-1 if it is missing)
        userId = getIntent().getLongExtra(UserActivity.EXTRA_USER_ID, -1L);
        if (userId == -1L) {
            Toast.makeText(this, R.string.invalid_data, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Link the Java variables with the views in the XML
        textMessage = findViewById(R.id.textExpenseMessage);
        cardExpenses = findViewById(R.id.cardExpenses);
        textSummary = findViewById(R.id.textExpenseSummary);
        layoutExpenseItems = findViewById(R.id.layoutExpenseItems);

        loadExpenses();
    }

    /**
     * Asks the backend for the expenses of the user's house (GET /users/{userId}/expenses).
     * 200 -> show the expenses, 404 -> the user has no house, anything else -> error.
     */
    private void loadExpenses() {
        showMessage(R.string.expenses_loading);

        ApiClient.expenseApi().getExpenses(userId).enqueue(new Callback<ExpensesResponse>() {

            @Override
            public void onResponse(Call<ExpensesResponse> call, Response<ExpensesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showExpenses(response.body());
                } else if (response.code() == 404) {
                    // The user does not belong to any house
                    showMessage(R.string.expenses_not_in_house);
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<ExpensesResponse> call, Throwable t) {
                Log.e(TAG, "No se pudieron cargar los gastos", t);
                showError();
            }
        });
    }

    /**
     * Shows the summary and one row per expense.
     * Each row shows the total of the expense and how much each member has to pay.
     */
    private void showExpenses(ExpensesResponse data) {
        // Nothing to show yet: the house exists but has no expenses
        if (data.expenses == null || data.expenses.isEmpty()) {
            showMessage(R.string.expenses_empty);
            return;
        }

        textMessage.setVisibility(View.GONE);
        cardExpenses.setVisibility(View.VISIBLE);

        String people = getResources().getQuantityString(
                R.plurals.expense_people_count, (int) data.memberCount, (int) data.memberCount);
        textSummary.setText(getString(R.string.expenses_summary_format,
                formatAmount(data.totalAmount), people));

        // Remove the rows of a previous load, then add one row per expense
        layoutExpenseItems.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (ExpenseResponse expense : data.expenses) {
            View row = inflater.inflate(R.layout.item_expense, layoutExpenseItems, false);

            ((TextView) row.findViewById(R.id.textExpenseName))
                    .setText(getString(R.string.expense_item_format, expense.name, formatAmount(expense.amount)));

            ((TextView) row.findViewById(R.id.textExpenseDetail))
                    .setText(getString(R.string.expense_share_format, people, formatAmount(expense.sharePerPerson)));

            layoutExpenseItems.addView(row);
        }
    }

    /**
     * Shows only a message (loading, no house, no expenses) and hides the expense card.
     */
    private void showMessage(int messageResId) {
        cardExpenses.setVisibility(View.GONE);
        textMessage.setText(messageResId);
        textMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Shows an error when the expenses could not be loaded from the server.
     */
    private void showError() {
        cardExpenses.setVisibility(View.GONE);
        textMessage.setText(R.string.server_unreachable);
        textMessage.setVisibility(View.VISIBLE);
        Toast.makeText(this, R.string.server_unreachable, Toast.LENGTH_LONG).show();
    }

    /**
     * Formats an amount without useless zeros: 80.00 -> "80", 16.50 -> "16.5".
     */
    private String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return "0";
        }
        return amount.stripTrailingZeros().toPlainString();
    }
}
