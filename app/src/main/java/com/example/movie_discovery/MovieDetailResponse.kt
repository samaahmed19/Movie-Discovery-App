package com.example.movie_discovery

data class MovieDetailsResponse(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val vote_average: Double?,     // ⭐ Add rating
    val release_date: String?      // 📅 Add release date
)
