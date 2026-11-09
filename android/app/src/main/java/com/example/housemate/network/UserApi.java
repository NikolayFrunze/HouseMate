package com.example.housemate.network;

import com.example.housemate.dto.CreateUserRequest;
import com.example.housemate.dto.LoginRequest;
import com.example.housemate.dto.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Describes the HTTP endpoints of the backend related to users.
 * Retrofit generates the code that actually sends these requests.
 */
public interface UserApi {

    /**
     * Creates a new user: POST {BASE_URL}users
     * The data is sent as JSON in the body of the request.
     */
    @POST("users")
    Call<UserResponse> createUser(@Body CreateUserRequest request);

    /**
     * Checks the login: POST {BASE_URL}users/login
     * The backend answers with the user data (200) or with 401 if the credentials are wrong.
     */
    @POST("users/login")
    Call<UserResponse> login(@Body LoginRequest request);
}
