package com.example.logo.api

import com.example.logo.model.NewsResponse
import com.example.logo.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("api/v4/top-headlines?lang=en") //api/v4/top-headlines?token=0e3b3454be42230c4cd2a3e2c67b3f48
    suspend fun getNews(
        @Query("token")
        apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    @GET("api/v4/search?q=example&lang=en") //?token=0e3b3454be42230c4cd2a3e2c67b3f48
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

    @GET("api/v4/search?q=example&lang=en") //?token=0e3b3454be42230c4cd2a3e2c67b3f48
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

    /*@GET("api/v4/search?q=example&lang=en") //?token=0e3b3454be42230c4cd2a3e2c67b3f48
    suspend fun searchNewsIn(
        @Query("in")
        searchQueryFrom: String,
        @Query("token")
        apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>*/
}