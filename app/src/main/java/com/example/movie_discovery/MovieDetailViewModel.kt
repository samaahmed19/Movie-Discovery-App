package com.example.movie_discovery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_discovery.MovieDetailsResponse
import com.example.movie_discovery.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val apiService = RetrofitInstance.api
    private val apiKey = "2745135cf88bf117b5ace2b3fbabf113"

    private val _movieDetails = MutableStateFlow<MovieDetailsResponse?>(null)
    val movieDetails: StateFlow<MovieDetailsResponse?> = _movieDetails.asStateFlow()

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieDetails.value = apiService.getMovieDetails(movieId, apiKey)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}




