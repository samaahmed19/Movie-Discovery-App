package com.example.movie_discovery.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_discovery.Networking.RetrofitInstance
import com.example.movie_discovery.data.CastMember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrailerViewModel : ViewModel() {

    private val api = RetrofitInstance.api
    private val apiKey = "2745135cf88bf117b5ace2b3fbabf113"

    private val _cast = MutableStateFlow<List<CastMember>>(emptyList())
    val cast: StateFlow<List<CastMember>> = _cast

    fun getMovieCast(movieId: Int, language: String) {
        viewModelScope.launch {
            try {
                val langCode = if (language == "ar") "ar-SA" else "en-US"

                val response = api.getMovieCredits(movieId, apiKey, langCode)
                _cast.value = response.cast.take(15)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}