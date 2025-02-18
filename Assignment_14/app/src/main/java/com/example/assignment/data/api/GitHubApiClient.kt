package com.example.assignment.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GitHubApiClient {
    private const val BASE_URL = "https://api.github.com/"

    val apiService: GitHubInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubInterface::class.java)
    }
}