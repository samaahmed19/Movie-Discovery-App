package com.example.movie_discovery.Viewmodels

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

data class UserSettings(
    val language: String = "en",
    val fontType: String = "Momo",
    val fontSize: Float = 18f
)

class SettingsViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    private val _userSettings = MutableStateFlow(UserSettings())
    val userSettings: StateFlow<UserSettings> = _userSettings

    init {
        loadUserSettings()
    }

    private fun loadUserSettings() {
        val user = auth.currentUser ?: return
        val docRef = firestore.collection("users").document(user.uid)
        docRef.addSnapshotListener { document, error ->
            if (error != null) return@addSnapshotListener
            if (document != null && document.exists()) {
                val settings = UserSettings(
                    language = document.getString("language") ?: "en",
                    fontType = document.getString("fontType") ?: "Momo",
                    fontSize = (document.getDouble("fontSize") ?: 18.0).toFloat()
                )
                _userSettings.value = settings
            } else {
                docRef.set(_userSettings.value)
            }
        }
    }

    fun changeLanguage(lang: String, context: Context) {
        updateSetting("language", lang)
        applyLocale(lang, context)
    }

    fun changeFontType(font: String) {
        updateSetting("fontType", font)
    }

    fun changeFontSize(size: Float) {
        updateSetting("fontSize", size)
    }

    fun resetToDefault(context: Context) {
        changeLanguage("en", context)
        changeFontType("Momo")
        changeFontSize(18f)
    }

    private fun updateSetting(field: String, value: Any) {
        val user = auth.currentUser ?: return
        val docRef = firestore.collection("users").document(user.uid)
        _userSettings.value = when (field) {
            "language" -> _userSettings.value.copy(language = value as String)
            "fontType" -> _userSettings.value.copy(fontType = value as String)
            "fontSize" -> _userSettings.value.copy(fontSize = value as Float)
            else -> _userSettings.value
        }

        docRef.update(field, value)
    }

    private fun applyLocale(lang: String, context: Context) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        (context as? android.app.Activity)?.recreate()
    }
}

