package com.example.movie_discovery.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "user_settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val FONT_TYPE_KEY = stringPreferencesKey("font_type")
        private val FONT_SIZE_KEY = floatPreferencesKey("font_size")
    }


    suspend fun saveLanguage(language: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language
        }
    }


    suspend fun saveFontType(fontType: String) {
        context.dataStore.edit { prefs ->
            prefs[FONT_TYPE_KEY] = fontType
        }
    }


    suspend fun saveFontSize(size: Float) {
        context.dataStore.edit { prefs ->
            prefs[FONT_SIZE_KEY] = size
        }
    }


    val languageFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[LANGUAGE_KEY] ?: "en"
    }

    val fontTypeFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[FONT_TYPE_KEY] ?: "Poppins"
    }

    val fontSizeFlow: Flow<Float> = context.dataStore.data.map { prefs ->
        prefs[FONT_SIZE_KEY] ?: 18f
    }
}
