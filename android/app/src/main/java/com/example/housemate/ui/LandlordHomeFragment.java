package com.example.housemate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.housemate.R;

public class LandlordHomeFragment extends Fragment {
    // Keys used to read the data sent by LoginActivity
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_EMAIL = "email";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView welcome = view.findViewById(R.id.textWelcome);

        // Read the data that LoginActivity put in the Intent
        Intent intent = requireActivity().getIntent();
        String username = intent.getStringExtra(EXTRA_USERNAME);
        // Show "Welcome, <username>" if the name was received
        if (username != null) {
            welcome.setText(getString(R.string.welcome_user, username));
        }

        // "House" button: open HouseActivity and pass the user id to it
        view.findViewById(R.id.buttonHouse).setOnClickListener(v -> {
            Intent houseIntent = new Intent(requireContext(), HouseActivity.class);
            houseIntent.putExtra(EXTRA_USER_ID, intent.getLongExtra(EXTRA_USER_ID, -1L));
            startActivity(houseIntent);
        });

        // "Expenses" button: open ExpenseActivity and pass the user id to it
        view.findViewById(R.id.buttonExpenses).setOnClickListener(v -> {
            Intent expensesIntent = new Intent(requireContext(), ExpenseActivity.class);
            expensesIntent.putExtra(EXTRA_USER_ID, intent.getLongExtra(EXTRA_USER_ID, -1L));
            startActivity(expensesIntent);
        });
    }
}
