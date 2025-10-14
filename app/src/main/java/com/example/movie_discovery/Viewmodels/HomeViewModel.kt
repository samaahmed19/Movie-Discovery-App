package com.example.movie_discovery.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_discovery.Networking.RetrofitInstance
import com.example.movie_discovery.data.MovieResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val apiKey = "2745135cf88bf117b5ace2b3fbabf113"
    private val api = RetrofitInstance.api

    private val _trendingMovies = MutableStateFlow<MovieResponse?>(null)
    val trendingMovies: StateFlow<MovieResponse?> = _trendingMovies.asStateFlow()

    private val _popularMovies = MutableStateFlow<MovieResponse?>(null)
    val popularMovies: StateFlow<MovieResponse?> = _popularMovies.asStateFlow()

    fun fetchMovies() {
        viewModelScope.launch {
            try {
                _trendingMovies.value = api.getTrendingMovies(apiKey)
                _popularMovies.value = api.getPopularMovies(apiKey)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
