package com.example.movie_discovery.data

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    val id: Int,
    val cast: List<CastMember>
)

data class CastMember(
    val id: Int,
    val name: String,
    val character: String,
    @SerializedName("profile_path")
    val profilePath: String?
)