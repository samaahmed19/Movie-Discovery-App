package com.example.movie_discovery.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _selectedLanguage = MutableStateFlow("en") // Default English
    val selectedLanguage = _selectedLanguage.asStateFlow()

    fun changeLanguage(lang: String) {
        viewModelScope.launch {
            _selectedLanguage.value = lang

        }
    }
}
