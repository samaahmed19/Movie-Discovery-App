package com.example.movie_discovery.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_discovery.data.MovieDetailsResponse
import com.example.movie_discovery.Networking.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val apiService = RetrofitInstance.api
    private val apiKey = "2745135cf88bf117b5ace2b3fbabf113"

    private val _movieDetails = MutableStateFlow<MovieDetailsResponse?>(null)
    val movieDetails: StateFlow<MovieDetailsResponse?> = _movieDetails.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun getMovieDetails(movieId: Int, language: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = apiService.getMovieDetails(movieId, apiKey, language)
                if (language.startsWith("ar")) {
                    val titleMissing = response.title.isNullOrBlank()
                    val overviewMissing = response.overview.isNullOrBlank()
                    val genresMissing = response.genres.isNullOrEmpty()
                    if (titleMissing || overviewMissing || genresMissing) {
                        val englishResponse = apiService.getMovieDetails(movieId, apiKey, "en-US")

                        val mergedResponse = response.copy(
                            title = if (titleMissing) englishResponse.title else response.title,
                            overview = if (overviewMissing) englishResponse.overview else response.overview,
                            genres = if (genresMissing) englishResponse.genres else response.genres
                        )

                        _movieDetails.value = mergedResponse
                    } else {
                        _movieDetails.value = response
                    }
                } else {
                    _movieDetails.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
