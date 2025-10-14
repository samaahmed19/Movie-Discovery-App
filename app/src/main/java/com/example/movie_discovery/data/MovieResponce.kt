package com.example.movie_discovery.data

data class MovieResponse(val results: List<MovieDetailsResponse>)



data class Movie(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val overview: String,
    val release_date: String
)