package com.example.logo.repository

import com.example.logo.api.RetrofitInstance
import com.example.logo.database.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getNews() =
        RetrofitInstance.api.getNews()

    suspend fun searchNews(searchQuery: String, searchQueryIn: String, searchQuerySortBy: String)=
        RetrofitInstance.api.searchNews(searchQuery, searchQueryIn, searchQuerySortBy)

    suspend fun searchNewsFromTo(searchQueryFrom: String, searchQueryTo: String, searchQueryIn: String)=
        RetrofitInstance.api.searchNewsFromTo(searchQueryFrom, searchQueryTo, searchQueryIn)


}