package com.example.movie_discovery.com.example.movie_discovery.Networking

import com.example.movie_discovery.com.example.movie_discovery.Networking.MovieApiService

object RetrofitInstance {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val api: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}