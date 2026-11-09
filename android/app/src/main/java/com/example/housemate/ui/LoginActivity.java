package com.example.housemate.ui;

import com.example.housemate.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housemate.network.ApiClient;
import com.example.housemate.dto.LoginRequest;
import com.example.housemate.dto.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * First screen of the app.
 * The user types an email and a password. If the backend confirms that the
 * user exists with that password, we open {@link UserActivity}.
 * The "Register" button opens {@link RegisterActivity} instead.
 */
public class LoginActivity extends AppCompatActivity {

    // Input fields (email and password)
    EditText email, password;
    // "Login" and "Register" buttons
    Button login, register;

    // Called by Android when the screen is created.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the layout XML (res/layout/login_activity.xml)
        setContentView(R.layout.login_activity);

        // Link the Java variables with the views defined in the XML using their ids
        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.button);
        register = findViewById(R.id.button2);

        // When "Login" is pressed
        login.setOnClickListener(view -> {
            // Read the text typed by the user (trim() removes spaces before/after the email)
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString();

            // Do not call the server if a field is empty
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, R.string.invalid_data, Toast.LENGTH_SHORT).show();
                return;
            }

            // Ask the backend if this user exists
            doLogin(emailText, passwordText);
        });


        // When "Register" is pressed, go to the registration screen
        register.setOnClickListener(view -> {

            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);

        });
    }

    /**
     * Sends the email and password to the backend (POST /users/login).
     * The request runs in the background; the answer is handled in the callback.
     */
    private void doLogin(String emailText, String passwordText) {
        // Disable the button so the user cannot press it twice while waiting
        login.setEnabled(false);

        ApiClient.userApi()
                // Build the request (not sent yet)
                .login(new LoginRequest(emailText, passwordText))
                // Send it in the background and receive the answer in the callback.
                // The type <UserResponse> tells Retrofit to convert the JSON into a UserResponse.
                .enqueue(new Callback<UserResponse>() {

                    // Called when the server answered, even with an error code (401, 500...)
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        // The answer arrived, so the button can be used again
                        login.setEnabled(true);

                        // HTTP 2xx and a body: the user exists and the password is correct
                        if (response.isSuccessful() && response.body() != null) {
                            openUserWindow(response.body());
                        // HTTP 401 (set by the backend): wrong email or password
                        } else if (response.code() == 401) {
                            Toast.makeText(LoginActivity.this, R.string.invalid_credentials, Toast.LENGTH_LONG).show();
                        // Any other error code from the server
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.invalid_data, Toast.LENGTH_LONG).show();
                        }
                    }

                    // Called when there was no answer at all (no network, server down, timeout...)
                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        // Write the technical error to Logcat to help debugging
                        Log.e("LoginActivity", "No se pudo completar el login", t);
                        login.setEnabled(true);
                        Toast.makeText(LoginActivity.this, R.string.server_unreachable, Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Opens the user window and passes the user data to it.
     * The login screen is closed so pressing "Back" does not return to it.
     */
    private void openUserWindow(UserResponse user) {
        Intent intent = new Intent(this, UserActivity.class);
        // Extras are the data sent to the next screen (read in UserActivity)
        intent.putExtra(UserActivity.EXTRA_USER_ID, user.id);
        intent.putExtra(UserActivity.EXTRA_USERNAME, user.username);
        intent.putExtra(UserActivity.EXTRA_EMAIL, user.email);
        startActivity(intent);
        finish();
    }
}
