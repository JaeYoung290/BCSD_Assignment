package com.example.assignment.data.api

import com.example.assignment.data.model.GitHubRepoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubInterface {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int
    ): GitHubRepoResponse
}