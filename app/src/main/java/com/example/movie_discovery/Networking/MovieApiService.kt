package com.example.movie_discovery.Networking

import com.example.movie_discovery.data.MovieDetailsResponse
import com.example.movie_discovery.data.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    //  Get popular movies
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    // Get trending movies (weekly)
    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String
    ): MovieResponse

    // ✅Get movie details by ID
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailsResponse
}
