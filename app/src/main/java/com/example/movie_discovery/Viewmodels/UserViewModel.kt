package com.example.movie_discovery.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie_discovery.Networking.RetrofitInstance
import com.example.movie_discovery.data.MovieDetailsResponse
import com.example.movie_discovery.data.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    private val _favouriteMovies = MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val favouriteMovies: StateFlow<List<MovieDetailsResponse>> = _favouriteMovies

    private val _watchlistMovies = MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val watchlistMovies: StateFlow<List<MovieDetailsResponse>> = _watchlistMovies

    private val _watchedMovies = MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val watchedMovies: StateFlow<List<MovieDetailsResponse>> = _watchedMovies

    init {
        loadUserData()
    }


    fun loadUserData() {
        val user = auth.currentUser ?: return
        val userDocRef = firestore.collection("users").document(user.uid)

        userDocRef.addSnapshotListener { document, error ->
            if (error != null) {
                _userData.value = UserData(
                    userId = "error",
                    firstName = "Error",
                    lastName = "",
                    email = "Error loading data: ${error.message ?: "Unknown error"}"
                )
                return@addSnapshotListener
            }
            if (document != null && document.exists()) {
                val userData = UserData(
                    userId = document.getString("userId") ?: user.uid,
                    firstName = document.getString("firstName") ?: "Guest",
                    lastName = document.getString("lastName") ?: "",
                    email = document.getString("email") ?: user.email.orEmpty(),
                    favourites = (document.get("favourites") as? List<*>)?.filterIsInstance<String>()
                        ?: emptyList(),
                    watchlist = (document.get("watchlist") as? List<*>)?.filterIsInstance<String>()
                        ?: emptyList(),
                    watched = (document.get("watched") as? List<*>)?.filterIsInstance<String>()
                        ?: emptyList(),
                    isDarkMode = document.getBoolean("isDarkMode") ?: false,
                    language = document.getString("language") ?: "en",
                    fontType = document.getString("fontType") ?: "Poppins",
                    fontSize = (document.getDouble("fontSize") ?: 16.0).toFloat()
                )
                _userData.value = userData

                loadMovieDetails(userData.favourites, _favouriteMovies)
                loadMovieDetails(userData.watchlist, _watchlistMovies)
                loadMovieDetails(userData.watched, _watchedMovies)
            } else {
                val newUser = UserData(
                    userId = user.uid,
                    firstName = user.displayName ?: "Guest",
                    lastName = "",
                    email = user.email ?: "",
                    isDarkMode = false,
                    language = "en",
                    fontType = "Poppins",
                    fontSize = 16f
                )
                firestore.collection("users").document(user.uid).set(newUser)
                _userData.value = newUser
            }
        }

    }
    fun addToFavourites(movieId: String, onFailure: () -> Unit = {}) =
        updateUserListField("favourites", movieId, true,onFailure)
    fun removeFromFavourites(movieId: String,onFailure: () -> Unit = {}) = updateUserListField("favourites", movieId, false, onFailure)
    fun addToWatchlist(movieId: String,onFailure: () -> Unit = {}) {
        updateUserListField("watchlist", movieId, true, onFailure)
        updateUserListField("watched", movieId, false)
    }
    fun removeFromWatchlist(movieId: String,onFailure: () -> Unit = {}) = updateUserListField("watchlist", movieId, false, onFailure)
    fun markAsWatched(movieId: String,onFailure: () -> Unit = {}) {
        updateUserListField("watched", movieId, true, onFailure)
        updateUserListField("watchlist", movieId, false)
    }
    fun unmarkFromWatched(movieId: String,onFailure: () -> Unit = {}) = updateUserListField("watched", movieId, false, onFailure)

    private fun updateUserListField(fieldName: String, movieId: String, add: Boolean,onFailure: () -> Unit = {}) {
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
            .addOnFailureListener {
                onFailure()
                it.printStackTrace()
            }
    }

    private fun loadMovieDetails(movieIds: List<String>, targetFlow: MutableStateFlow<List<MovieDetailsResponse>>) {
        if (movieIds.isEmpty()) {
            targetFlow.value = emptyList()
            return
        }

        viewModelScope.launch {
            val movies = mutableListOf<MovieDetailsResponse>()
            movieIds.forEach { movieId ->
                try {
                    val movie = getMovieDetailsFromTMDB(movieId.toInt())
                    movie?.let { movies.add(it) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            targetFlow.value = movies
        }
    }
    suspend fun getMovieDetailsFromTMDB(movieId: Int): MovieDetailsResponse? {
        return try {
                RetrofitInstance.api.getMovieDetails(
                    movieId,
                    "2745135cf88bf117b5ace2b3fbabf113"
                )
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
