package com.example.housemate.network;

import com.example.housemate.dto.CreateHouseRequest;
import com.example.housemate.dto.HouseResponse;
import com.example.housemate.dto.JoinHouseRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Describes the HTTP endpoints of the backend related to houses.
 * Retrofit generates the code that actually sends these requests.
 */
public interface HouseApi {

    /**
     * Gets the house of a user: GET {BASE_URL}users/{userId}/house
     * Answers 200 with the house, or 404 if the user has no house yet.
     */
    @GET("users/{userId}/house")
    Call<HouseResponse> getHouse(@Path("userId") long userId);

    /**
     * Registers the house of a user: POST {BASE_URL}users/{userId}/house
     * Answers 2xx with the saved house, or 409 if the user already has a house.
     */
    @POST("users/{userId}/house")
    Call<HouseResponse> createHouse(@Path("userId") long userId, @Body CreateHouseRequest request);

    @POST("users/{userId}/house/join")
    Call<HouseResponse> joinHouse(@Path("userId") long userId, @Body JoinHouseRequest request);
}
