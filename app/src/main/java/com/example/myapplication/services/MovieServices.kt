package com.example.myapplication.services

import com.example.myapplication.model.Movie
import com.example.myapplication.model.TVShow
import com.example.myapplication.model.Trending
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface MovieServices{
    @GET("movie/popular?api_key=10f7dce517a35d8b04696b9fb88b412f&language=en-US&page=1")
    abstract fun getTrending(): Call<Trending>

    @GET("trending/movie/week?api_key=10f7dce517a35d8b04696b9fb88b412f")
    abstract fun getTrendingMovies(): Call<Trending>

    @GET("trending/tv/week?api_key=10f7dce517a35d8b04696b9fb88b412f")
    abstract fun getTrendingTVShows(): Call<Trending>

    @GET
    abstract fun getMovie(@Url string: String): Call<Movie>

    @GET
    abstract fun getTVShow(@Url string: String): Call<TVShow>

    @GET
    abstract fun getRecommendations(@Url string: String): Call<Trending>
}