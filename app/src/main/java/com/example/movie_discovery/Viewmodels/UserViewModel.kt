package com.example.movie_discovery.Viewmodels

import android.content.Context
import android.widget.Toast
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
import com.google.firebase.firestore.ListenerRegistration
class UserViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    private var userSnapshotListener: ListenerRegistration? = null

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

        userSnapshotListener?.remove()
        userSnapshotListener = userDocRef.addSnapshotListener { document, error ->

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
                userDocRef.set(newUser)
                _userData.value = newUser
            }
        }
    }

    fun addToFavourites(movieId: String, onFailure: () -> Unit = {}) {
        updateUserListField("favourites", movieId, true, onFailure)
        viewModelScope.launch {
            if (_favouriteMovies.value.none { it.id.toString() == movieId }) {
                getMovieDetailsFromTMDB(movieId.toInt())?.let { movie ->
                    _favouriteMovies.value = _favouriteMovies.value + movie
                }
            }
        }
    }

    fun removeFromFavourites(movieId: String, onFailure: () -> Unit = {}) {
        updateUserListField("favourites", movieId, false, onFailure)
        _favouriteMovies.value =
            _favouriteMovies.value.filterNot { it.id.toString() == movieId }
    }

    fun addToWatchlist(movieId: String, onFailure: () -> Unit = {}) {
        updateWatchlistAndWatched(
            movieId,
            addToWatchlist = true,
            removeFromWatched = true,
            onFailure = onFailure
        )
    }

    fun removeFromWatchlist(movieId: String, onFailure: () -> Unit = {}) {
        updateUserListField("watchlist", movieId, false, onFailure)
    }

    fun markAsWatched(movieId: String, onFailure: () -> Unit = {}) {
        updateWatchlistAndWatched(
            movieId,
            addToWatched = true,
            removeFromWatchlist = true,
            onFailure = onFailure
        )
    }

    fun unmarkFromWatched(movieId: String, onFailure: () -> Unit = {}) =
        updateUserListField("watched", movieId, false, onFailure)

    private fun updateWatchlistAndWatched(
        movieId: String,
        addToWatchlist: Boolean = false,
        removeFromWatchlist: Boolean = false,
        addToWatched: Boolean = false,
        removeFromWatched: Boolean = false,
        onFailure: () -> Unit = {}
    ) {
        val user = auth.currentUser ?: return
        val docRef = firestore.collection("users").document(user.uid)

        val current = _userData.value ?: return

        val updatedWatchlist = current.watchlist
            .toMutableList()
            .apply {
                if (addToWatchlist && movieId !in this) add(movieId)
                if (removeFromWatchlist) remove(movieId)
            }

        val updatedWatched = current.watched
            .toMutableList()
            .apply {
                if (addToWatched && movieId !in this) add(movieId)
                if (removeFromWatched) remove(movieId)
            }

        _userData.value = current.copy(
            watchlist = updatedWatchlist,
            watched = updatedWatched
        )

        firestore.runTransaction { tx ->
            tx.update(docRef, "watchlist", updatedWatchlist)
            tx.update(docRef, "watched", updatedWatched)
        }.addOnFailureListener { onFailure() }

        viewModelScope.launch {

            if (addToWatchlist) {
                if (_watchlistMovies.value.none { it.id.toString() == movieId }) {
                    getMovieDetailsFromTMDB(movieId.toInt())?.let { movie ->
                        _watchlistMovies.value = _watchlistMovies.value + movie
                    }
                }
            }
            if (removeFromWatchlist) {
                _watchlistMovies.value =
                    _watchlistMovies.value.filterNot { it.id.toString() == movieId }
            }

            if (addToWatched) {
                if (_watchedMovies.value.none { it.id.toString() == movieId }) {
                    getMovieDetailsFromTMDB(movieId.toInt())?.let { movie ->
                        _watchedMovies.value = _watchedMovies.value + movie
                    }
                }
            }
            if (removeFromWatched) {
                _watchedMovies.value =
                    _watchedMovies.value.filterNot { it.id.toString() == movieId }
            }
        }
    }

    private fun updateUserListField(
        fieldName: String,
        movieId: String,
        add: Boolean,
        onFailure: () -> Unit = {}
    ) {
        val user = auth.currentUser ?: return
        val docRef = firestore.collection("users").document(user.uid)
        val current = _userData.value ?: return

        val updatedList = when (fieldName) {
            "favourites" -> if (add) current.favourites + movieId else current.favourites - movieId
            "watchlist" -> if (add) current.watchlist + movieId else current.watchlist - movieId
            "watched" -> if (add) current.watched + movieId else current.watched - movieId
            else -> return
        }.distinct()

        _userData.value = when (fieldName) {
            "favourites" -> current.copy(favourites = updatedList)
            "watchlist" -> current.copy(watchlist = updatedList)
            else -> current.copy(watched = updatedList)
        }
        firestore.runTransaction { tx ->
            tx.update(docRef, fieldName, updatedList)
        }.addOnFailureListener {
            onFailure()
            it.printStackTrace()
        }
    }

    private fun loadMovieDetails(
        ids: List<String>,
        target: MutableStateFlow<List<MovieDetailsResponse>>
    ) {
        if (ids.isEmpty()) {
            target.value = emptyList()
            return
        }

        viewModelScope.launch {
            target.value = ids.mapNotNull { id ->
                try {
                    getMovieDetailsFromTMDB(id.toInt())
                } catch (e: Exception) {
                    null
                }
            }
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
            firestore.collection("users").document(user.uid).delete()
                .addOnSuccessListener {
                    user.delete()
                        .addOnSuccessListener {
                            _userData.value = null
                            onComplete?.invoke(true)
                        }
                        .addOnFailureListener {
                            onComplete?.invoke(false)
                        }
                }
                .addOnFailureListener {
                    onComplete?.invoke(false)
                }
        } else {
            onComplete?.invoke(false)
        }
    }
}