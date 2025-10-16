package com.example.movie_discovery.data

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val results: List<MovieDetailsResponse>
)

data class MovieSearchResponse(
    val results: List<Movie> = emptyList(),
    val page: Int = 1,
    @SerializedName("total_pages") val totalPages: Int = 0
)
