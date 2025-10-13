package com.example.movie_discovery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_discovery.BuildConfig
import com.example.movie_discovery.MovieDetailsResponse
import com.example.movie_discovery.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val _movieDetail = MutableStateFlow<MovieDetailsResponse?>(null)
    val movieDetail: StateFlow<MovieDetailsResponse?> = _movieDetail

    fun loadMovieDetail(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMovieDetails(
                    movieId = movieId,
                    apiKey = BuildConfig.TMDB_API_KEY
                )
                _movieDetail.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



