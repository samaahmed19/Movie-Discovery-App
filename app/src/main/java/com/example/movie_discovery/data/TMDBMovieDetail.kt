package com.example.movie_discovery.data

data class TMDBMovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String? = null
)