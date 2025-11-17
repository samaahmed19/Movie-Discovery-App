package com.example.movie_discovery.data

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    val id: Int,
    val results: List<VideoResult>
)
data class VideoResult(
    val id: String,
    @SerializedName("iso_639_1")
    val language: String,
    val name: String,
    val key: String,
    val site: String,
    val type: String
)
