package com.example.logo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logo.model.Article
import com.example.logo.model.NewsResponse
import com.example.logo.repository.NewsRepository
import com.example.logo.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository): ViewModel()  {

    val news: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNewsFromTo: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNewsIn: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    //val news: MutableLiveData<Article> = MutableLiveData()

    /*var lang = "en"
    var newsPage = 1*/

    init {
        getNews() //"us", "en"
    }

   /* fun getNews(){

        val article1 = Article(1,"content","desc","image","puslish","title","String")
     *//*   val list = arrayListOf<Article>(article1)
        val newsResponse = NewsResponse(list,1)
        val res = Resource() *//*


        news.value = article1
    }*/

    fun getNews() = viewModelScope.launch { //countryCode: String, language: String
        news.postValue(Resource.Loading())
        val response = newsRepository.getNews() //countryCode,lang,newsPage
        news.postValue(handleNewsResponse(response))
    }

    fun searchNews(searchQuery: String, searchQueryIn: String, searchQuerySortBy: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery,searchQueryIn,searchQuerySortBy)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    fun searchNewsFromTo(searchQueryFrom: String, searchQueryTo: String, searchQueryIn: String) = viewModelScope.launch {
        searchNewsFromTo.postValue(Resource.Loading())
        val response = newsRepository.searchNewsFromTo(searchQueryFrom,searchQueryTo, searchQueryIn)
        searchNewsFromTo.postValue(handleSearchNewsFromToResponse(response))
    }

   /* fun searchNewsIn(searchIn: String) = viewModelScope.launch {
        searchNewsIn.postValue(Resource.Loading())
        val response = newsRepository.searchNewsIn(searchIn)
        searchNewsIn.postValue(handleSearchNewsInResponse(response))
    }*/

    private fun handleNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsFromToResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /*private fun handleSearchNewsInResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }*/
}