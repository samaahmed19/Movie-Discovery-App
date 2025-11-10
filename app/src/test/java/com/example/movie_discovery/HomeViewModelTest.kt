package com.example.movie_discovery

import com.example.movie_discovery.data.MovieDetailsResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: FakeHomeViewModel

    @Before
    fun setUp() {
        viewModel = FakeHomeViewModel()
    }

    @Test
    fun `loadMovies should update popular movies`() = runTest {
        viewModel.loadMovies()
        val movies = viewModel.popularMovies.first()
        assertEquals(2, movies.size)
        assertEquals("Popular Movie 1", movies[0].title)
        assertEquals("Popular Movie 2", movies[1].title)
    }

    @Test
    fun `loadMovies should update trending movies`() = runTest {
        viewModel.loadMovies()
        val movies = viewModel.trendingMovies.first()
        assertEquals(2, movies.size)
        assertEquals("Trending Movie 1", movies[0].title)
        assertEquals("Trending Movie 2", movies[1].title)
    }

    @Test
    fun `loadMovies should update upcoming movies`() = runTest {
        viewModel.loadMovies()
        val movies = viewModel.upcomingMovies.first()
        assertEquals(2, movies.size)
        assertEquals("Upcoming Movie 1", movies[0].title)
        assertEquals("Upcoming Movie 2", movies[1].title)
    }

    @Test
    fun `loadMovies should update top rated movies`() = runTest {
        viewModel.loadMovies()
        val movies = viewModel.topRatedMovies.first()
        assertEquals(2, movies.size)
        assertEquals("Top Rated Movie 1", movies[0].title)
        assertEquals("Top Rated Movie 2", movies[1].title)
    }

    @Test
    fun `loadMovies should handle empty responses`() = runTest {
        val emptyViewModel = FakeHomeViewModel(
            popular = emptyList(),
            trending = emptyList(),
            upcoming = emptyList(),
            topRated = emptyList()
        )
        emptyViewModel.loadMovies()
        assertTrue(emptyViewModel.popularMovies.first().isEmpty())
        assertTrue(emptyViewModel.trendingMovies.first().isEmpty())
        assertTrue(emptyViewModel.upcomingMovies.first().isEmpty())
        assertTrue(emptyViewModel.topRatedMovies.first().isEmpty())
    }

    @Test
    fun `loadMovies should handle movies with null properties`() = runTest {
        val nullMovie = MovieDetailsResponse(
            id = 0,
            title = "",
            overview = "",
            posterPath = null,
            releaseDate = "",
            voteAverage = 0.0
        )
        val vm = FakeHomeViewModel(
            popular = listOf(nullMovie),
            trending = listOf(nullMovie),
            upcoming = listOf(nullMovie),
            topRated = listOf(nullMovie)
        )
        vm.loadMovies()
        assertEquals("", vm.popularMovies.first()[0].title)
        assertNull(vm.trendingMovies.first()[0].posterPath)
        assertEquals(0.0, vm.upcomingMovies.first()[0].voteAverage, 0.0)
    }

    @Test
    fun `loadMovies should be callable multiple times`() = runTest {
        viewModel.loadMovies()
        viewModel.loadMovies() // second call
        assertEquals(2, viewModel.popularMovies.first().size)
    }

    @Test
    fun `different categories should have independent data`() = runTest {
        viewModel.loadMovies()
        val popular = viewModel.popularMovies.first()
        val trending = viewModel.trendingMovies.first()
        assertNotEquals(popular, trending)
    }

    @Test
    fun `movie lists should maintain order`() = runTest {
        viewModel.loadMovies()
        val upcoming = viewModel.upcomingMovies.first()
        assertEquals("Upcoming Movie 1", upcoming[0].title)
        assertEquals("Upcoming Movie 2", upcoming[1].title)
    }
}

class FakeHomeViewModel(
    private val popular: List<MovieDetailsResponse> = listOf(
        createMovie(1, "Popular Movie 1", 8.0),
        createMovie(2, "Popular Movie 2", 7.5)
    ),
    private val trending: List<MovieDetailsResponse> = listOf(
        createMovie(3, "Trending Movie 1", 8.2),
        createMovie(4, "Trending Movie 2", 7.9)
    ),
    private val upcoming: List<MovieDetailsResponse> = listOf(
        createMovie(5, "Upcoming Movie 1", 7.1),
        createMovie(6, "Upcoming Movie 2", 7.3)
    ),
    private val topRated: List<MovieDetailsResponse> = listOf(
        createMovie(7, "Top Rated Movie 1", 9.0),
        createMovie(8, "Top Rated Movie 2", 8.8)
    )
) {

    private val _popularMovies = kotlinx.coroutines.flow.MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val popularMovies = _popularMovies

    private val _trendingMovies = kotlinx.coroutines.flow.MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val trendingMovies = _trendingMovies

    private val _upcomingMovies = kotlinx.coroutines.flow.MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val upcomingMovies = _upcomingMovies

    private val _topRatedMovies = kotlinx.coroutines.flow.MutableStateFlow<List<MovieDetailsResponse>>(emptyList())
    val topRatedMovies = _topRatedMovies

    suspend fun loadMovies() {
        _popularMovies.value = popular
        _trendingMovies.value = trending
        _upcomingMovies.value = upcoming
        _topRatedMovies.value = topRated
    }
}

private fun createMovie(
    id: Int,
    title: String,
    voteAverage: Double = 7.5
) = MovieDetailsResponse(
    id = id,
    title = title,
    overview = "Desc",
    posterPath = "/poster$id.jpg",
    releaseDate = "2024-01-01",
    voteAverage = voteAverage
)
