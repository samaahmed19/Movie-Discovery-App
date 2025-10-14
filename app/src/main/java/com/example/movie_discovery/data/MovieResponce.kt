package com.example.movie_discovery.data

import com.google.gson.annotations.SerializedName

data class MovieResponse(val results: List<MovieDetailsResponse>)

data class MovieSearchResponse(
    val results: List<Movie> = emptyList(),
    val page: Int = 1,
    @SerializedName("total_pages")
    val totalPages: Int = 0
)

data class CategoryMoviesResponse(
    val results: List<Movie> = emptyList(),
    val page: Int = 1,
    @SerializedName("total_pages")
    val totalPages: Int = 0
)

data class Movie(
    val id: Int = 0,
    val title: String = "",
    val overview: String,
    @SerializedName("poster_path")
    val poster_path: String? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @SerializedName("release_date")
    val release_date: String = "",
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,
    @SerializedName("genre_ids")
    val genreIds: List<Int> = emptyList()
)