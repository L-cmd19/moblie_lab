package com.example.afinal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface FitApiService {

    // Menarik daftar olahraga secara acak dengan batas jumlah tertentu
    @GET("exercises")
    Call<JsonArray> getExercises(
            @Header("X-RapidAPI-Key") String apiKey,
            @Header("X-RapidAPI-Host") String apiHost,
            @Query("limit") int limit
    );

    // Menarik kata motivasi secara acak
    @GET("https://dummyjson.com/quotes/random")
    Call<JsonObject> getRandomQuote();
}