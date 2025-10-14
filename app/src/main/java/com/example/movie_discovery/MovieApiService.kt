package com.example.movie_discovery

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    // ✅ Get movie details by movie ID
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailsResponse
}





