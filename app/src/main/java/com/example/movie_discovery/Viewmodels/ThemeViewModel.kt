package com.example.movie_discovery.Viewmodels


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class ThemeViewModel : ViewModel() {
    var isDarkMode by mutableStateOf(false)
        private set

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
}
