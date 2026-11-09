package com.example.housemate.ui;

import com.example.housemate.R;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.housemate.network.ApiClient;
import com.example.housemate.dto.CreateUserRequest;
import com.example.housemate.dto.UserResponse;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.fragment.NavHostFragment;

import com.example.housemate.databinding.RegisterActivityBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Screen where a new user creates an account.
 * It sends the username, email and password to the backend (POST /users).
 * On Android 17 (API 37) and newer, it also asks for the local network permission first.
 */
public class RegisterActivity extends AppCompatActivity {

    // Android 17 (API 37) blocks connections to the local network without this permission
    private static final String LOCAL_NETWORK_PERMISSION = "android.permission.ACCESS_LOCAL_NETWORK";
    // First Android version that requires the permission above
    private static final int ANDROID_17 = 37;

    // Input fields
    EditText username, email, password;
    // "Save" button
    Button save;

    // Launcher that shows the system permission dialog and receives the user's answer.
    // If granted, we create the account; if denied, we show a message.
    private final ActivityResultLauncher<String> localNetworkPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    createAccount();
                } else {
                    Toast.makeText(this, R.string.local_network_permission_required, Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the layout XML (res/layout/register_activity.xml)
        setContentView(R.layout.register_activity);

        // Link the Java variables with the views in the XML
        username = findViewById(R.id.editTextUsername);
        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        save = findViewById(R.id.buttonSave);

        // When "Save" is pressed
        save.setOnClickListener(view -> {
            // If the permission is needed and not granted yet, ask for it first.
            // Otherwise, create the account directly.
            if (Build.VERSION.SDK_INT >= ANDROID_17
                    && checkSelfPermission(LOCAL_NETWORK_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                localNetworkPermission.launch(LOCAL_NETWORK_PERMISSION);
            } else {
                createAccount();
            }
        });
    }

    /**
     * Reads the form and sends it to the backend to create the user (POST /users).
     */
    private void createAccount() {
        // Read the fields; trim() removes spaces at the start and end
        String usernameText = username.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString();

        // Disable the button while the request is running
        save.setEnabled(false);

        ApiClient.userApi()
                // Build the request with the data of the form
                .createUser(new CreateUserRequest(usernameText, emailText, passwordText))
                // Send it in the background and handle the answer in the callback
                .enqueue(new Callback<UserResponse>() {

                    // The server answered (any HTTP status code)
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        save.setEnabled(true);
                        // 2xx: the user was created
                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, R.string.account_created, Toast.LENGTH_SHORT).show();
                            clearFields();
                        // 409 Conflict: the backend says this email already exists
                        } else if (response.code() == 409) {
                            Toast.makeText(RegisterActivity.this, R.string.email_already_registered, Toast.LENGTH_LONG).show();
                        // Any other error (for example 400 when validation fails)
                        } else {
                            Toast.makeText(RegisterActivity.this, R.string.invalid_data, Toast.LENGTH_LONG).show();
                        }
                    }

                    // No answer from the server (no network, server down, timeout...)
                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Log.e("RegisterActivity", "No se pudo completar el registro", t);
                        save.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, R.string.server_unreachable, Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Empties the form after the account was created successfully.
     */
    private void clearFields() {
        username.setText("");
        email.setText("");
        password.setText("");
        // Put the cursor back in the first field
        username.requestFocus();
    }
}
