package com.example.movie_discovery.com.example.movie_discovery.Networking

import com.example.movie_discovery.MovieDetailsResponse

interface MovieApiService {

    // ✅ Get movie details by movie ID
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailsResponse
}