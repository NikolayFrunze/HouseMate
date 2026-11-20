package com.example.housemate.ui;

import static com.example.housemate.ui.LoginActivity.PREFS_NAME;

import com.example.housemate.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

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

    public static final String EXTRA_ROLE = "role";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the layout XML (res/layout/user_activity.xml)
        setContentView(R.layout.user_activity);

        SharedPreferences sharedPref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String role = sharedPref.getString(EXTRA_ROLE, "");

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();
        NavGraph graph = navController.getNavInflater().inflate(R.navigation.nav_graph);

        switch (role) {
            case "User":
                graph.setStartDestination(R.id.UserHomeFragment);
                break;
            case "Landlord":
                graph.setStartDestination(R.id.LandlordHomeFragment);
                break;
            default:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
        }
        navController.setGraph(graph);

    }
}
