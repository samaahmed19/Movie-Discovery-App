package com.example.movie_discovery.data


data class UserData(
    val userId: String = "",
    val firstName: String = "",
    val email: String = "",
    val favourites: List<String> = emptyList(),
    val watchlist: List<String> = emptyList(),
    val watched: List<String> = emptyList()
)
