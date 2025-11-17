package com.example.movie_discovery

import com.example.movie_discovery.Viewmodels.TrailerViewModel
import com.example.movie_discovery.Networking.MovieApiService
import com.example.movie_discovery.data.CastMember
import com.example.movie_discovery.data.CreditsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.reflect.Field

@OptIn(ExperimentalCoroutinesApi::class)
class TrailerViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var mockApi: MovieApiService
    private lateinit var viewModel: TrailerViewModel

    @Before
    fun setup() {
        mockApi = mock()
        viewModel = TrailerViewModel()

        val apiField: Field = TrailerViewModel::class.java.getDeclaredField("api")
        apiField.isAccessible = true
        apiField.set(viewModel, mockApi)
    }

    @Test
    fun `getMovieCast updates cast list successfully`() = runTest {
        val dummyCast = listOf(
            CastMember(1, "Actor 1", "Character 1", null),
            CastMember(2, "Actor 2", "Character 2", null)
        )
        val response = CreditsResponse(id = 101, cast = dummyCast)

        whenever(mockApi.getMovieCredits(any(), any(), any())).thenReturn(response)

        viewModel.getMovieCast(101, "en")

        assertEquals(2, viewModel.cast.value.size)
        assertEquals("Actor 1", viewModel.cast.value[0].name)
    }

    @Test
    fun `getMovieCast uses correct language code for Arabic`() = runTest {
        val response = CreditsResponse(101, emptyList())
        whenever(mockApi.getMovieCredits(any(), any(), any())).thenReturn(response)

        viewModel.getMovieCast(101, "ar")

        verify(mockApi).getMovieCredits(101, "2745135cf88bf117b5ace2b3fbabf113", "ar-SA")
    }

    @Test
    fun `getMovieCast takes only first 15 items`() = runTest {
        val longList = List(20) { CastMember(it, "Name $it", "Char $it", null) }
        val response = CreditsResponse(101, longList)

        whenever(mockApi.getMovieCredits(any(), any(), any())).thenReturn(response)

        viewModel.getMovieCast(101, "en")

        assertEquals(15, viewModel.cast.value.size)
    }

    @Test
    fun `getMovieCast handles exception gracefully`() = runTest {
        whenever(mockApi.getMovieCredits(any(), any(), any())).thenThrow(RuntimeException("Network Error"))

        viewModel.getMovieCast(101, "en")

        assertTrue(viewModel.cast.value.isEmpty())
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}