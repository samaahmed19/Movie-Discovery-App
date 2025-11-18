package com.example.movie_discovery.data

data class UserData(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val favourites: List<String> = emptyList(),
    val watchlist: List<String> = emptyList(),
    val watched: List<String> = emptyList(),
    val isDarkMode: Boolean = false,
    val language: String = "en",
    val fontType: String = "Poppins",
    val fontSize: Float = 16f
)
