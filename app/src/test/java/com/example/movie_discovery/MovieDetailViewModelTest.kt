package com.example.movie_discovery

import com.example.movie_discovery.Networking.MovieApiService
import com.example.movie_discovery.Viewmodels.MovieDetailViewModel
import com.example.movie_discovery.data.MovieDetailsResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var apiService: MovieApiService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        apiService = mockk()

        viewModel = MovieDetailViewModel().apply {
            val apiField = this::class.java.getDeclaredField("apiService")
            apiField.isAccessible = true
            apiField.set(this, apiService)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getMovieDetails_shouldUpdateMovieDetails_whenApiCallIsSuccessful() = runTest {

        val mockResponse = MovieDetailsResponse(
            id = 123,
            title = "Inception",
            overview = "A dream within a dream.",
            releaseDate = "2010-07-16",
            voteAverage = 8.8,
            posterPath = "/poster.jpg"
        )

        coEvery { apiService.getMovieDetails(any(), any()) } returns mockResponse


        viewModel.getMovieDetails(123)
        testDispatcher.scheduler.advanceUntilIdle()


        val result = viewModel.movieDetails.first()
        assertNotNull(result)
        assertEquals("Inception", result?.title)
        assertEquals(8.8, result?.voteAverage ?: 0.0, 0.01)
        assertNull(viewModel.error.first())
        assertFalse(viewModel.isLoading.first())
    }

    @Test
    fun getMovieDetails_shouldSetError_whenApiThrowsException() = runTest {

        coEvery { apiService.getMovieDetails(any(), any()) } throws RuntimeException("Network error")

        viewModel.getMovieDetails(123)
        testDispatcher.scheduler.advanceUntilIdle()


        val error = viewModel.error.first()
        assertEquals("Network error", error)
        assertNull(viewModel.movieDetails.first())
        assertFalse(viewModel.isLoading.first())
    }
}
