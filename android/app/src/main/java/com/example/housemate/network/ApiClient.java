package com.example.housemate.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creates and keeps the Retrofit client used to talk to the Spring Boot backend.
 * Only one instance is created (singleton) and it is reused by all screens.
 */
public final class ApiClient {

    // Address of the backend.
    // Emulator: 10.0.2.2 points to localhost of your PC.
    // Physical device: use the IP address of your PC on the local network.
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    // The single instance of the API interface
    private static UserApi userApi;
    // The single instance of the house API interface
    private static HouseApi houseApi;
    // The single instance of the expense API interface
    private static ExpenseApi expenseApi;

    // Private constructor: nobody can create an ApiClient object
    private ApiClient() {
    }

    /**
     * Returns the object used to call the user endpoints (UserApi).
     * It is created the first time it is needed.
     */
    public static synchronized UserApi userApi() {
        if (userApi == null) {
            // Retrofit builds the HTTP calls from the UserApi interface.
            // GsonConverterFactory converts JSON <-> Java objects automatically.
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // Create the implementation of UserApi
            userApi = retrofit.create(UserApi.class);
        }
        return userApi;
    }

    /**
     * Returns the object used to call the house endpoints (HouseApi).
     * It reuses the same Retrofit client configuration as userApi().
     */
    public static synchronized HouseApi houseApi() {
        if (houseApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            houseApi = retrofit.create(HouseApi.class);
        }
        return houseApi;
    }

    /**
     * Returns the object used to call the expense endpoints (ExpenseApi).
     * It reuses the same Retrofit client configuration as userApi().
     */
    public static synchronized ExpenseApi expenseApi() {
        if (expenseApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            expenseApi = retrofit.create(ExpenseApi.class);
        }
        return expenseApi;
    }
}
