package com.example.movie_discovery.data

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signUp(firstName: String, lastName: String, email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid ?: return@addOnCompleteListener

                    val userData = hashMapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email,
                        "watched" to emptyList<String>(),
                        "watchlist" to emptyList<String>(),
                        "favorites" to emptyList<String>(),
                        "isDarkMode" to false,
                        "language" to "en",
                        "fontType" to "Poppins",
                        "fontSize" to 16f
                    )

                    firestore.collection("users").document(userId)
                        .set(userData)
                        .addOnSuccessListener {
                            val actionCodeSettings = ActionCodeSettings.newBuilder()
                                .setUrl("https://movie-discovery-ab154.web.app/__/auth/action")
                                .setHandleCodeInApp(true)
                                .setAndroidPackageName("com.example.movie_discovery", true, "21")
                                .build()

                            user?.sendEmailVerification(actionCodeSettings)
                                ?.addOnSuccessListener {
                                    _authState.value = AuthState.Success(
                                        "Account created! Please check your email to verify before logging in."
                                    )
                                }
                                ?.addOnFailureListener { e ->
                                    _authState.value = AuthState.Error(
                                        "Account created, but failed to send verification email: ${e.message}"
                                    )
                                }
                        }
                        .addOnFailureListener { e ->
                            _authState.value = AuthState.Error(e.message ?: "Error saving data.")
                        }
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Sign up failed. Please try again.")
                }
            }
    }

    fun signIn(email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        _authState.value = AuthState.Success("Signed in successfully!")
                    } else {
                        auth.signOut()
                        _authState.value = AuthState.Error("Please verify your email before signing in.")
                    }
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Invalid credentials.")
                }
            }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
