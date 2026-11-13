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
import com.example.housemate.dto.JoinHouseRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Screen opened from the "House" button of {@link UserActivity}.
 * - If the user already belongs to a house, its data is shown (including the join code).
 * - If not, two buttons are shown: "Register with a code" and "Create a house".
 *   Each button opens its own form.
 *
 * Only one card is visible at a time: data, choice, join form or create form.
 */
public class HouseActivity extends AppCompatActivity {

    private static final String TAG = "HouseActivity";

    // Id of the logged user, received from UserActivity
    private long userId;

    // Message shown at the top ("You don't belong to any house", errors, loading...)
    private TextView textMessage;
    // Card with the data of the house (shown when the user has one)
    private View layoutHouseData;
    // Card with the two buttons "Register with a code" / "Create a house"
    private View layoutChoice;
    // Card with the form to join a house with its code
    private View layoutJoinForm;
    // Card with the form to register a house
    private View layoutForm;

    // Texts that show the saved house data
    private TextView textAddress, textPostalCode, textHousingCode, textJoinCode;
    // Fields of the create form
    private EditText editAddress, editPostalCode, editHousingCode;
    // Field of the join form
    private EditText editJoinCode;
    // Buttons of the choice card
    private Button buttonShowJoin, buttonShowCreate;
    // "Join" button of the join form and "Save" button of the create form
    private Button buttonJoin, buttonSave;

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
        layoutChoice = findViewById(R.id.layoutHouseChoice);
        layoutJoinForm = findViewById(R.id.layoutJoinForm);
        layoutForm = findViewById(R.id.layoutHouseForm);
        textAddress = findViewById(R.id.textHouseAddress);
        textPostalCode = findViewById(R.id.textHousePostalCode);
        textHousingCode = findViewById(R.id.textHouseHousingCode);
        textJoinCode = findViewById(R.id.textHouseJoinCode);
        editAddress = findViewById(R.id.editHouseAddress);
        editPostalCode = findViewById(R.id.editHousePostalCode);
        editHousingCode = findViewById(R.id.editHouseHousingCode);
        editJoinCode = findViewById(R.id.editJoinCode);
        buttonShowJoin = findViewById(R.id.buttonShowJoin);
        buttonShowCreate = findViewById(R.id.buttonShowCreate);
        buttonJoin = findViewById(R.id.buttonJoin);
        buttonSave = findViewById(R.id.buttonHouseSave);

        // Choice card: each button opens its own form
        buttonShowJoin.setOnClickListener(view -> showOnly(layoutJoinForm));
        buttonShowCreate.setOnClickListener(view -> showOnly(layoutForm));

        // Forms: "Join" joins a house with a code, "Save" registers a new house
        buttonJoin.setOnClickListener(view -> joinHouse());
        buttonSave.setOnClickListener(view -> saveHouse());

        // Ask the backend whether this user already belongs to a house
        loadHouse();
    }

    /**
     * Shows only the given card and hides all the others.
     * Pass null to hide every card (for example while loading or after an error).
     */
    private void showOnly(View card) {
        layoutHouseData.setVisibility(card == layoutHouseData ? View.VISIBLE : View.GONE);
        layoutChoice.setVisibility(card == layoutChoice ? View.VISIBLE : View.GONE);
        layoutJoinForm.setVisibility(card == layoutJoinForm ? View.VISIBLE : View.GONE);
        layoutForm.setVisibility(card == layoutForm ? View.VISIBLE : View.GONE);
    }

    /**
     * Asks the backend for the house of the user (GET /users/{userId}/house).
     * 200 -> show the house data, 404 -> show the two buttons.
     */
    private void loadHouse() {
        textMessage.setText(R.string.house_loading);
        textMessage.setVisibility(View.VISIBLE);
        showOnly(null);

        ApiClient.houseApi().getHouse(userId).enqueue(new Callback<HouseResponse>() {

            @Override
            public void onResponse(Call<HouseResponse> call, Response<HouseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // The user already belongs to a house: show its data
                    showHouse(response.body());
                } else if (response.code() == 404) {
                    // The user has no house: show the message and the two buttons
                    showChoice();
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
     * Shows the data of the house the user belongs to, including the join code
     * that the user can share with the other housemates.
     */
    private void showHouse(HouseResponse house) {
        textMessage.setVisibility(View.GONE);

        textAddress.setText(getString(R.string.house_address_format, house.address));
        textPostalCode.setText(getString(R.string.house_postal_code_format, house.postalCode));
        textHousingCode.setText(getString(R.string.house_housing_code_format, house.housingCode));
        textJoinCode.setText(getString(R.string.house_join_code_format, house.joinCode));
        showOnly(layoutHouseData);
    }

    /**
     * Shows the message "You don't belong to any house" and the two buttons
     * "Register with a code" and "Create a house".
     */
    private void showChoice() {
        textMessage.setText(R.string.house_not_in_any);
        textMessage.setVisibility(View.VISIBLE);
        showOnly(layoutChoice);
    }

    /**
     * Shows an error when the house could not be loaded from the server.
     */
    private void showError() {
        showOnly(null);
        textMessage.setText(R.string.server_unreachable);
        textMessage.setVisibility(View.VISIBLE);
        Toast.makeText(this, R.string.server_unreachable, Toast.LENGTH_LONG).show();
    }

    /**
     * Reads the code and joins the house that has it (POST /users/{userId}/house/join).
     * 200 -> reload to show the house, 404 -> wrong code, 409 -> already in a house.
     */
    private void joinHouse() {
        String joinCode = editJoinCode.getText().toString().trim();

        // Do not call the server if the field is empty
        if (joinCode.isEmpty()) {
            Toast.makeText(this, R.string.house_fields_required, Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable the button while the request is running
        buttonJoin.setEnabled(false);

        ApiClient.houseApi()
                .joinHouse(userId, new JoinHouseRequest(joinCode))
                .enqueue(new Callback<HouseResponse>() {

                    @Override
                    public void onResponse(Call<HouseResponse> call, Response<HouseResponse> response) {
                        buttonJoin.setEnabled(true);
                        if (response.isSuccessful()) {
                            // Joined: reload so the house data is shown
                            Toast.makeText(HouseActivity.this, R.string.house_joined, Toast.LENGTH_SHORT).show();
                            loadHouse();
                        } else if (response.code() == 404) {
                            // No house has this code
                            Toast.makeText(HouseActivity.this, R.string.house_invalid_code, Toast.LENGTH_LONG).show();
                        } else if (response.code() == 409) {
                            // The user already belongs to a house: reload to show it
                            Toast.makeText(HouseActivity.this, R.string.house_already_assigned, Toast.LENGTH_LONG).show();
                            loadHouse();
                        } else {
                            Toast.makeText(HouseActivity.this, R.string.invalid_data, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<HouseResponse> call, Throwable t) {
                        Log.e(TAG, "No se pudo unir a la casa", t);
                        buttonJoin.setEnabled(true);
                        Toast.makeText(HouseActivity.this, R.string.server_unreachable, Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Reads the form and registers the house (POST /users/{userId}/house).
     * On success the house is reloaded so the user sees it with its join code.
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
                            // House saved: reload so the user sees it and its join code
                            Toast.makeText(HouseActivity.this, R.string.house_saved, Toast.LENGTH_SHORT).show();
                            loadHouse();
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
