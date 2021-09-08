package com.example.logo.model

data class NewsResponse(
    val articles: List<Article>,
    val totalArticles: Int
)