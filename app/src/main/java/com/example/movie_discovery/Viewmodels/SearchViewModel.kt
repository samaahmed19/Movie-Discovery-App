package com.example.movie_discovery.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_discovery.Networking.RetrofitInstance
import com.example.movie_discovery.data.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Query

class SearchViewModel : ViewModel() {
    private val apiService = RetrofitInstance.api

    private val apiKey = "2745135cf88bf117b5ace2b3fbabf113"

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults.asStateFlow()

    private val _moviesByGenre = MutableStateFlow<List<Movie>>(emptyList())
    val moviesByGenre: StateFlow<List<Movie>> = _moviesByGenre.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }

    fun searchMovies(query: String){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                /* val response = apiService.searchMovies(apiKey , query)
                _searchResults.value = response.results */
            } catch (e: Exception){
                _error.value = e.message
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getMoviesByGenre(genreId: Int){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                /* val response = apiService.discoverByGenre(apiKey, genreId)
                _moviesByGenre.value = response.results */
            } catch (e: Exception) {
                _error.value = e.message
                _moviesByGenre.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

}