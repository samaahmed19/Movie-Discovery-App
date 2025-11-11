package com.example.movie_discovery
import android.content.Context
import com.example.movie_discovery.Viewmodels.UserSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private lateinit var viewModel: FakeSettingsViewModel
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        viewModel = FakeSettingsViewModel()
        mockContext = Mockito.mock(Context::class.java)
    }

    @Test
    fun changeLanguage_shouldUpdateLanguage() = runTest {
        viewModel.changeLanguage("ar", mockContext)
        val settings = viewModel.userSettings.first()
        assertEquals("ar", settings.language)
    }

    @Test
    fun changeFontType_shouldUpdateFontType() = runTest {
        viewModel.changeFontType("Cairo")
        val settings = viewModel.userSettings.first()
        assertEquals("Cairo", settings.fontType)
    }

