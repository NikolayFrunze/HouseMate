package com.example.housemate.ui;

import com.example.housemate.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main window of the user after a successful login.
 * It receives the user data from {@link LoginActivity} through Intent extras.
 * More user information will be added here later.
 */
public class UserActivity extends AppCompatActivity {

    // Keys used to read the data sent by LoginActivity
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the layout XML (res/layout/user_activity.xml)
        setContentView(R.layout.user_activity);

        TextView welcome = findViewById(R.id.textWelcome);

        // Read the data that LoginActivity put in the Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra(EXTRA_USERNAME);
        // Show "Welcome, <username>" if the name was received
        if (username != null) {
            welcome.setText(getString(R.string.welcome_user, username));
        }

        // "House" button: open HouseActivity and pass the user id to it
        findViewById(R.id.buttonHouse).setOnClickListener(view -> {
            Intent houseIntent = new Intent(this, HouseActivity.class);
            houseIntent.putExtra(EXTRA_USER_ID, intent.getLongExtra(EXTRA_USER_ID, -1L));
            startActivity(houseIntent);
        });

        // "Expenses" button: open ExpenseActivity and pass the user id to it
        findViewById(R.id.buttonExpenses).setOnClickListener(view -> {
            Intent expensesIntent = new Intent(this, ExpenseActivity.class);
            expensesIntent.putExtra(EXTRA_USER_ID, intent.getLongExtra(EXTRA_USER_ID, -1L));
            startActivity(expensesIntent);
        });
    }
}
