package com.example.movie_discovery.data

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val isArabic = Locale.getDefault().language.startsWith("ar")

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    val language = MutableStateFlow("en")
    val fontType = MutableStateFlow("Poppins")
    val fontSize = MutableStateFlow(16f)

    fun signUp(firstName: String, lastName: String, email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser ?: return@addOnCompleteListener
                    val userId = user.uid

                    val userData = hashMapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email,
                        "watched" to emptyList<String>(),
                        "watchlist" to emptyList<String>(),
                        "favorites" to emptyList<String>(),
                        "isDarkMode" to false,
                        "language" to language.value,
                        "fontType" to fontType.value,
                        "fontSize" to fontSize.value
                    )

                    firestore.collection("users").document(userId)
                        .set(userData)
                        .addOnSuccessListener {
                            val actionCodeSettings = ActionCodeSettings.newBuilder()
                                .setUrl("https://movie-discovery-ab154.web.app/__/auth/action")
                                .setHandleCodeInApp(true)
                                .setAndroidPackageName("com.example.movie_discovery", true, "21")
                                .build()

                            user.sendEmailVerification(actionCodeSettings)
                                .addOnSuccessListener {
                                    _authState.value = AuthState.Success(
                                        if (isArabic)
                                            "تم إنشاء الحساب! تحقق من بريدك الإلكتروني لتفعيله قبل تسجيل الدخول."
                                        else
                                            "Account created! Please check your email to verify before logging in."
                                    )
                                }
                                .addOnFailureListener { e ->
                                    _authState.value = AuthState.Error(
                                        if (isArabic)
                                            "تم إنشاء الحساب، لكن فشل إرسال رسالة التفعيل: ${e.message}"
                                        else
                                            "Account created, but failed to send verification email: ${e.message}"
                                    )
                                }
                        }
                        .addOnFailureListener { e ->
                            _authState.value = AuthState.Error(
                                e.message ?: if (isArabic) "حدث خطأ أثناء حفظ البيانات." else "Error saving data."
                            )
                        }
                } else {
                    _authState.value = AuthState.Error(
                        task.exception?.message ?: if (isArabic) "فشل إنشاء الحساب، حاول مرة أخرى." else "Sign up failed. Please try again."
                    )
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
                        loadUserSettings(user.uid)
                        _authState.value = AuthState.Success(
                            if (isArabic) "تم تسجيل الدخول بنجاح!" else "Signed in successfully!"
                        )
                    } else {
                        auth.signOut()
                        _authState.value = AuthState.Error(
                            if (isArabic) "يرجى تفعيل بريدك الإلكتروني أولاً." else "Please verify your email first."
                        )
                    }
                } else {
                    _authState.value = AuthState.Error(
                        task.exception?.message ?: if (isArabic) "بيانات الدخول غير صحيحة." else "Invalid credentials."
                    )
                }
            }
    }

    private fun loadUserSettings(userId: String) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { doc ->
                language.value = doc.getString("language") ?: "en"
                fontType.value = doc.getString("fontType") ?: "Poppins"
                fontSize.value = (doc.getDouble("fontSize") ?: 16.0).toFloat()
            }
    }

    fun updateUserSetting(field: String, value: Any) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).update(field, value)

        when (field) {
            "language" -> language.value = value as String
            "fontType" -> fontType.value = value as String
            "fontSize" -> fontSize.value = value as Float
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


