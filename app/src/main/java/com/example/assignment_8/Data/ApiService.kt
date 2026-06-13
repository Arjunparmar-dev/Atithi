package com.example.assignment_8.Data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("uploads/android/hotelbooking/places.json")
    suspend fun getPlaces(): List<Place>
}

object RetrofitInstance {
    private const val BASE_URL = "https://trainings.internshala.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}