package com.example.movie_discovery.Viewmodels


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {
    var isDarkMode by mutableStateOf(false)
        private set

    private val _fontType = MutableStateFlow("Roboto")
    val fontType: StateFlow<String> = _fontType

    private val _fontSize = MutableStateFlow(16f)
    val fontSize: StateFlow<Float> = _fontSize
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    fun loadDarkMode(defaultDarkMode: Boolean) {
        val user = auth.currentUser ?: run {
            isDarkMode = defaultDarkMode
            return
        }

        firestore.collection("users").document(user.uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists() && doc.contains("darkMode")) {
                    isDarkMode = doc.getBoolean("darkMode") ?: defaultDarkMode
                } else {
                    isDarkMode = defaultDarkMode
                    firestore.collection("users").document(user.uid)
                        .set(mapOf("darkMode" to isDarkMode), SetOptions.merge())
                }
            }
            .addOnFailureListener {
                isDarkMode = defaultDarkMode
            }
    }


    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
        val user = auth.currentUser ?: return
        firestore.collection("users").document(user.uid)
            .update("darkMode", isDarkMode)
    }
    fun setFontType(type: String) {
        viewModelScope.launch {
            _fontType.value = type
        }
        fun setFontSize(size: Float) {
            viewModelScope.launch {
                _fontSize.value = size
            }
        }
    }
}
