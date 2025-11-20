package com.example.movie_discovery.Viewmodels


import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel : ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isThemeLoaded = MutableStateFlow(false)
    val isThemeLoaded: StateFlow<Boolean> = _isThemeLoaded.asStateFlow()

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    fun loadDarkMode(defaultDarkMode: Boolean) {
        val user = auth.currentUser
        if (user == null) {
            _isDarkMode.value = defaultDarkMode
            _isThemeLoaded.value = true
            return
        }

        firestore.collection("users").document(user.uid)
            .get()
            .addOnSuccessListener { doc ->
                _isDarkMode.value = doc.getBoolean("isDarkMode") ?: defaultDarkMode
                _isThemeLoaded.value = true
            }
            .addOnFailureListener {
                _isDarkMode.value = defaultDarkMode
                _isThemeLoaded.value = true
            }
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
        val user = auth.currentUser ?: return
        firestore.collection("users").document(user.uid)
            .update("isDarkMode", _isDarkMode.value)
    }
}
