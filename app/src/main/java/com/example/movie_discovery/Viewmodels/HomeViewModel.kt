package com.example.movie_discovery.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_discovery.Networking.RetrofitInstance
import com.example.movie_discovery.data.MovieDetailsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val apiService = RetrofitInstance.api
    private val apiKey = "2745135cf88bf117b5ace2b3fbabf113"

    private val _popularMovies = MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val popularMovies: StateFlow<List<MovieDetailsResponse>> = _popularMovies.asStateFlow()

    private val _trendingMovies = MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val trendingMovies: StateFlow<List<MovieDetailsResponse>> = _trendingMovies.asStateFlow()

    private val _upcomingMovies = MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val upcomingMovies: StateFlow<List<MovieDetailsResponse>> = _upcomingMovies.asStateFlow()

    private val _topRatedMovies = MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val topRatedMovies: StateFlow<List<MovieDetailsResponse>> = _topRatedMovies.asStateFlow()

    fun loadMovies() {
        viewModelScope.launch {
            try {
                val popular = apiService.getPopularMovies(apiKey).results
                val trending = apiService.getTrendingMovies(apiKey).results
                val upcoming = apiService.getUpcomingMovies(apiKey).results
                val topRated = apiService.getTopRatedMovies(apiKey).results

                _popularMovies.value = popular
                _trendingMovies.value = trending
                _upcomingMovies.value = upcoming
                _topRatedMovies.value = topRated
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
