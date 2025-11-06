package com.example.movie_discovery.Viewmodels

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_discovery.data.SettingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsViewModel(private val dataStore: SettingsDataStore) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow("en")
    val selectedLanguage = _selectedLanguage.asStateFlow()

    private val _fontSize = MutableStateFlow(18f)
    val fontSize = _fontSize.asStateFlow()

    private val _fontType = MutableStateFlow("Cairo")
    val fontType = _fontType.asStateFlow()

    init {
        viewModelScope.launch { dataStore.languageFlow.collect { _selectedLanguage.value = it } }
        viewModelScope.launch { dataStore.fontTypeFlow.collect { _fontType.value = it } }
        viewModelScope.launch { dataStore.fontSizeFlow.collect { _fontSize.value = it } }
    }

    fun changeLanguage(lang: String, context: Context? = null) {
        _selectedLanguage.value = lang
        viewModelScope.launch { dataStore.saveLanguage(lang) }
        context?.let { setLocale(it, lang) }
    }

    fun changeFontSize(size: Float) {
        _fontSize.value = size
        viewModelScope.launch { dataStore.saveFontSize(size) }
    }

    fun changeFontType(type: String) {
        _fontType.value = type
        viewModelScope.launch { dataStore.saveFontType(type) }
    }

    fun resetToDefault() {
        viewModelScope.launch {
            dataStore.saveLanguage("en")
            dataStore.saveFontType("Cairo")
            dataStore.saveFontSize(18f)
        }
        _selectedLanguage.value = "en"
        _fontType.value = "Cairo"
        _fontSize.value = 18f
    }

    private fun setLocale(context: Context, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        (context as? Activity)?.recreate()
    }
}
