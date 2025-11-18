package com.example.movie_discovery.Viewmodels


import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel : ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _fontType = MutableStateFlow("Roboto")
    val fontType: StateFlow<String> = _fontType

    private val _fontSize = MutableStateFlow(16f)
    val fontSize: StateFlow<Float> = _fontSize
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    fun loadDarkMode(defaultDarkMode: Boolean) {
        val user = auth.currentUser ?: run {
            _isDarkMode.value = defaultDarkMode
            return
        }

        firestore.collection("users").document(user.uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists() && doc.contains("isDarkMode")) {
                    _isDarkMode.value = doc.getBoolean("isDarkMode") ?: defaultDarkMode
                } else {
                    _isDarkMode.value = defaultDarkMode
                    firestore.collection("users").document(user.uid)
                        .set(mapOf("isDarkMode" to defaultDarkMode), SetOptions.merge())
                }
            }
            .addOnFailureListener {
                _isDarkMode.value = defaultDarkMode
                }
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
        val user = auth.currentUser ?: return
        firestore.collection("users").document(user.uid)
            .update("isDarkMode", _isDarkMode.value)
    }
}