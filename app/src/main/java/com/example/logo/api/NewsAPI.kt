package com.example.logo.api

import com.example.logo.model.NewsResponse
import com.example.logo.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("api/v4/top-headlines?lang=en")
    suspend fun getNews(
        @Query("token")
        apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    @GET("api/v4/search?q=example&lang=en")
    suspend fun searchNews(
        @Query("q")
        searchQuery: String,
        @Query("in")
        searchQueryIn: String,
        @Query("sortby")
        searchQuerySortBy: String,
        @Query("token")
        apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    @GET("api/v4/search?q=example&lang=en")
    suspend fun searchNewsFromTo(
        @Query("from")
        searchQueryFrom: String,
        @Query("to")
        searchQueryTo: String,
        @Query("in")
        searchQueryIn: String,
        @Query("token")
        apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>
}