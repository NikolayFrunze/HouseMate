package com.example.housemate.ui;

import com.example.housemate.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.housemate.network.ApiClient;
import com.example.housemate.dto.CreateHouseRequest;
import com.example.housemate.dto.HouseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Screen opened from the "House" button of {@link UserActivity}.
 * - If the user already has a house in the backend, its data is shown.
 * - If not, a message is shown together with a form to register the house.
 * After saving, this screen closes and the user goes back to {@link UserActivity}.
 */
public class HouseActivity extends AppCompatActivity {

    private static final String TAG = "HouseActivity";

    // Id of the logged user, received from UserActivity
    private long userId;

    // Message shown at the top ("You don't belong to any house", errors, loading...)
    private TextView textMessage;
    // Card with the data of the house (shown when the user has one)
    private View layoutHouseData;
    // Card with the form to register a house (shown when the user has none)
    private View layoutForm;

    // Texts that show the saved house data
    private TextView textAddress, textPostalCode, textHousingCode;
    // Form fields
    private EditText editAddress, editPostalCode, editHousingCode;
    // "Save" button of the form
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_activity);

        // Read the user id sent by UserActivity (-1 if it is missing)
        userId = getIntent().getLongExtra(UserActivity.EXTRA_USER_ID, -1L);
        if (userId == -1L) {
            Toast.makeText(this, R.string.invalid_data, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Link the Java variables with the views in the XML
        textMessage = findViewById(R.id.textHouseMessage);
        layoutHouseData = findViewById(R.id.layoutHouseData);
        layoutForm = findViewById(R.id.layoutHouseForm);
        textAddress = findViewById(R.id.textHouseAddress);
        textPostalCode = findViewById(R.id.textHousePostalCode);
        textHousingCode = findViewById(R.id.textHouseHousingCode);
        editAddress = findViewById(R.id.editHouseAddress);
        editPostalCode = findViewById(R.id.editHousePostalCode);
        editHousingCode = findViewById(R.id.editHouseHousingCode);
        buttonSave = findViewById(R.id.buttonHouseSave);

        // When "Save" is pressed, register the house
        buttonSave.setOnClickListener(view -> saveHouse());

        // Ask the backend whether this user already has a house
        loadHouse();
    }

    /**
     * Asks the backend for the house of the user (GET /users/{userId}/house).
     * 200 -> show the house data, 404 -> show the form.
     */
    private void loadHouse() {
        textMessage.setText(R.string.house_loading);
        textMessage.setVisibility(View.VISIBLE);
        layoutHouseData.setVisibility(View.GONE);
        layoutForm.setVisibility(View.GONE);

        ApiClient.houseApi().getHouse(userId).enqueue(new Callback<HouseResponse>() {

            @Override
            public void onResponse(Call<HouseResponse> call, Response<HouseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // The user already has a house: show its data
                    showHouse(response.body());
                } else if (response.code() == 404) {
                    // The user has no house: show the message and the form
                    showForm();
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<HouseResponse> call, Throwable t) {
                Log.e(TAG, "No se pudo cargar la casa", t);
                showError();
            }
        });
    }

    /**
     * Shows the data of a house that is already registered.
     */
    private void showHouse(HouseResponse house) {
        textMessage.setVisibility(View.GONE);
        layoutForm.setVisibility(View.GONE);

        textAddress.setText(getString(R.string.house_address_format, house.address));
        textPostalCode.setText(getString(R.string.house_postal_code_format, house.postalCode));
        textHousingCode.setText(getString(R.string.house_housing_code_format, house.housingCode));
        layoutHouseData.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the message "You don't belong to any house" and the form to register one.
     */
    private void showForm() {
        layoutHouseData.setVisibility(View.GONE);
        textMessage.setText(R.string.house_not_in_any);
        textMessage.setVisibility(View.VISIBLE);
        layoutForm.setVisibility(View.VISIBLE);
    }

    /**
     * Shows an error when the house could not be loaded from the server.
     */
    private void showError() {
        layoutHouseData.setVisibility(View.GONE);
        layoutForm.setVisibility(View.GONE);
        textMessage.setText(R.string.server_unreachable);
        textMessage.setVisibility(View.VISIBLE);
        Toast.makeText(this, R.string.server_unreachable, Toast.LENGTH_LONG).show();
    }

    /**
     * Reads the form and registers the house (POST /users/{userId}/house).
     * On success the screen closes and the user returns to UserActivity.
     */
    private void saveHouse() {
        String address = editAddress.getText().toString().trim();
        String postalCode = editPostalCode.getText().toString().trim();
        String housingCode = editHousingCode.getText().toString().trim();

        // Do not call the server if a field is empty
        if (address.isEmpty() || postalCode.isEmpty() || housingCode.isEmpty()) {
            Toast.makeText(this, R.string.house_fields_required, Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable the button while the request is running
        buttonSave.setEnabled(false);

        ApiClient.houseApi()
                .createHouse(userId, new CreateHouseRequest(address, postalCode, housingCode))
                .enqueue(new Callback<HouseResponse>() {

                    @Override
                    public void onResponse(Call<HouseResponse> call, Response<HouseResponse> response) {
                        buttonSave.setEnabled(true);
                        if (response.isSuccessful()) {
                            // House saved: close this screen and go back to UserActivity
                            Toast.makeText(HouseActivity.this, R.string.house_saved, Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (response.code() == 409) {
                            // The user already has a house: reload to show it
                            Toast.makeText(HouseActivity.this, R.string.house_already_assigned, Toast.LENGTH_LONG).show();
                            loadHouse();
                        } else {
                            Toast.makeText(HouseActivity.this, R.string.invalid_data, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<HouseResponse> call, Throwable t) {
                        Log.e(TAG, "No se pudo guardar la casa", t);
                        buttonSave.setEnabled(true);
                        Toast.makeText(HouseActivity.this, R.string.server_unreachable, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
