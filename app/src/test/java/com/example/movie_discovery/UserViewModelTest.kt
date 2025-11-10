package com.example.movie_discovery

    import com.example.movie_discovery.data.MovieDetailsResponse
    import com.example.movie_discovery.data.UserData
    import kotlinx.coroutines.ExperimentalCoroutinesApi
    import kotlinx.coroutines.flow.first
    import kotlinx.coroutines.test.runTest
    import org.junit.Assert.*
    import org.junit.Before
    import org.junit.Test

    @OptIn(ExperimentalCoroutinesApi::class)
    class UserViewModelTest {

        private lateinit var viewModel: FakeUserViewModel

        @Before
        fun setUp() {
            viewModel = FakeUserViewModel()
        }

        @Test
        fun addMovieToFavourites() = runTest {
            viewModel.addToFavourites("100")
            val data = viewModel.userData.first()
            assertTrue(data?.favourites?.contains("100") == true)
        }

        @Test
        fun removeMovieFromFavourites() = runTest {
            viewModel.addToFavourites("200")
            viewModel.removeFromFavourites("200")
            val data = viewModel.userData.first()
            assertFalse(data?.favourites?.contains("200") == true)
        }

        @Test
        fun addToWatchlist() = runTest {
            viewModel.addToWatchlist("300")
            val data = viewModel.userData.first()
            assertEquals(listOf("300"), data?.watchlist)
        }

        @Test
        fun removeFromWatchlist() = runTest {
            viewModel.addToWatchlist("400")
            viewModel.removeFromWatchlist("400")
            val data = viewModel.userData.first()
            assertEquals(emptyList<String>(), data?.watchlist)
        }

        @Test
        fun markAsWatched() = runTest {
            viewModel.markAsWatched("500")
            val data = viewModel.userData.first()
            assertEquals(listOf("500"), data?.watched)
        }

        @Test
        fun unmarkFromWatched() = runTest {
            viewModel.markAsWatched("600")
            viewModel.unmarkFromWatched("600")
            val data = viewModel.userData.first()
            assertEquals(emptyList<String>(), data?.watched)
        }

        @Test
        fun getMovieDetailsFromTMDBReturnsDummyData() = runTest {
            val details = viewModel.getMovieDetailsFromTMDB(123)
            assertNotNull(details)
            assertEquals(123, details?.id)
            assertEquals("Dummy Movie", details?.title)
        }
    }

    class FakeUserViewModel {

        private val _userData = kotlinx.coroutines.flow.MutableStateFlow(
            UserData(
                userId = "testUser",
                firstName = "Tester",
                email = "test@example.com",
                favourites = emptyList(),
                watchlist = emptyList(),
                watched = emptyList(),
                isDarkMode = false
            )
        )
        val userData: kotlinx.coroutines.flow.StateFlow<UserData?> = _userData

        fun addToFavourites(movieId: String) = updateUserList("favourites", movieId, true)
        fun removeFromFavourites(movieId: String) = updateUserList("favourites", movieId, false)
        fun addToWatchlist(movieId: String) = updateUserList("watchlist", movieId, true)
        fun removeFromWatchlist(movieId: String) = updateUserList("watchlist", movieId, false)
        fun markAsWatched(movieId: String) = updateUserList("watched", movieId, true)
        fun unmarkFromWatched(movieId: String) = updateUserList("watched", movieId, false)

        private fun updateUserList(field: String, movieId: String, add: Boolean) {
            val current = _userData.value ?: return
            val updated = when (field) {
                "favourites" -> current.copy(
                    favourites = if (add) current.favourites + movieId else current.favourites - movieId
                )
                "watchlist" -> current.copy(
                    watchlist = if (add) current.watchlist + movieId else current.watchlist - movieId
                )
                "watched" -> current.copy(
                    watched = if (add) current.watched + movieId else current.watched - movieId
                )
                else -> current
            }
            _userData.value = updated
        }

        fun getMovieDetailsFromTMDB(movieId: Int): MovieDetailsResponse {
            return MovieDetailsResponse(
                id = movieId,
                title = "Dummy Movie",
                overview = "This is a fake movie used for testing.",
                posterPath = "/fakePoster.jpg",
                releaseDate = "2024-01-01",
                voteAverage = 7.5
            )
        }
    }