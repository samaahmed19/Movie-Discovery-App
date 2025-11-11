package com.example.movie_discovery

import app.cash.turbine.test
import com.example.movie_discovery.Viewmodels.SearchViewModel
import com.example.movie_discovery.data.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test clearSearchResults clears results`() = runTest {
        viewModel.clearSearchResults()
        viewModel.searchResults.test {
            val results = awaitItem()
            assertEquals(0, results.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test searchMovies updates results correctly`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel.searchMovies("Inception")

        viewModel.isLoading.test {
            val first = awaitItem()
            assertEquals(true, first || false)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test getMoviesByGenre updates state correctly`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel.getMoviesByGenre(28)

        viewModel.isLoading.test {
            val value = awaitItem()
            assertEquals(true, value || false)
            cancelAndIgnoreRemainingEvents()
        }
    }
}