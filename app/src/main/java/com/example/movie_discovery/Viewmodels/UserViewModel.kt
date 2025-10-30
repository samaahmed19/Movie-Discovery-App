package com.example.movie_discovery.Viewmodels

import androidx.lifecycle.ViewModel
import com.example.movie_discovery.Networking.RetrofitInstance
import com.example.movie_discovery.data.MovieDetailsResponse
import com.example.movie_discovery.data.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

class UserViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    init {
        loadUserData()
    }


    fun loadUserData() {
        val user = auth.currentUser ?: return
        val userDocRef = firestore.collection("users").document(user.uid)

        userDocRef.addSnapshotListener { document, error ->
            if (error != null) {
                _userData.value = UserData(
                    firstName = "Error",
                    email = "Error loading data"
                )
                return@addSnapshotListener
            }
            if (document != null && document.exists()) {
                val userData = UserData(
                    userId = document.getString("userId") ?: user.uid,
                    firstName = document.getString("firstName") ?: "Guest",
                    email = document.getString("email") ?: user.email.orEmpty(),
                    favourites = (document.get("favourites") as? List<*>)?.filterIsInstance<String>()
                        ?: emptyList(),
                    watchlist = (document.get("watchlist") as? List<*>)?.filterIsInstance<String>()
                        ?: emptyList(),
                    watched = (document.get("watched") as? List<*>)?.filterIsInstance<String>()
                        ?: emptyList(),
                    isDarkMode = document.getBoolean("isDarkMode") ?: false
                )
                _userData.value = userData
            } else {
                val newUser = UserData(
                    userId = user.uid,
                    firstName = user.displayName ?: "Guest",
                    email = user.email ?: "",
                    isDarkMode = false
                )
                firestore.collection("users").document(user.uid).set(newUser)
                _userData.value = newUser
            }
        }

    }
            fun addToFavourites(movieId: String) = updateUserListField("favourites", movieId, true)
    fun removeFromFavourites(movieId: String) = updateUserListField("favourites", movieId, false)
    fun addToWatchlist(movieId: String) = updateUserListField("watchlist", movieId, true)
    fun removeFromWatchlist(movieId: String) = updateUserListField("watchlist", movieId, false)
    fun markAsWatched(movieId: String) = updateUserListField("watched", movieId, true)
    fun unmarkFromWatched(movieId: String) = updateUserListField("watched", movieId, false)

    private fun updateUserListField(fieldName: String, movieId: String, add: Boolean) {
        val currentData = _userData.value ?: return
        val updatedData = when (fieldName) {
            "favourites" -> currentData.copy(
                favourites = if (add) currentData.favourites + movieId else currentData.favourites - movieId
            )
            "watchlist" -> currentData.copy(
                watchlist = if (add) currentData.watchlist + movieId else currentData.watchlist - movieId
            )
            "watched" -> currentData.copy(
                watched = if (add) currentData.watched + movieId else currentData.watched - movieId
            )
            else -> currentData
        }
        _userData.value = updatedData
        val user = auth.currentUser ?: return
        val docRef = firestore.collection("users").document(user.uid)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val currentList = snapshot.get(fieldName) as? List<String> ?: emptyList()
            val updatedList = if (add) currentList + movieId else currentList - movieId
            transaction.update(docRef, fieldName, updatedList.distinct())
        }
    }


    fun getMovieDetailsFromTMDB(movieId: Int): MovieDetailsResponse? {
        return try {
            runBlocking {
                RetrofitInstance.api.getMovieDetails(
                    movieId,
                    "2745135cf88bf117b5ace2b3fbabf113"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun logout(onComplete: (() -> Unit)? = null) {
        auth.signOut()
        _userData.value = null
        onComplete?.invoke()
    }

    fun deleteAccount(onComplete: ((Boolean) -> Unit)? = null) {
        val user = auth.currentUser
        if (user != null) {
            val userDoc = firestore.collection("users").document(user.uid)
            userDoc.delete().addOnSuccessListener {
                user.delete()
                    .addOnSuccessListener {
                        _userData.value = null
                        onComplete?.invoke(true)
                    }
                    .addOnFailureListener {
                        onComplete?.invoke(false)
                    }
            }.addOnFailureListener {
                onComplete?.invoke(false)
            }
        } else {
            onComplete?.invoke(false)
        }
    }
}

