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

    @Test
    fun changeFontSize_shouldUpdateFontSize() = runTest {
        viewModel.changeFontSize(22f)
        val settings = viewModel.userSettings.first()
        assertEquals(22f, settings.fontSize)
    }

    @Test
    fun resetToDefault_shouldReturnDefaultValues() = runTest {
        viewModel.changeLanguage("ar", mockContext)
        viewModel.changeFontType("Roboto")
        viewModel.changeFontSize(20f)
        viewModel.resetToDefault(mockContext)
        val settings = viewModel.userSettings.first()
        assertEquals("en", settings.language)
        assertEquals("Momo", settings.fontType)
        assertEquals(18f, settings.fontSize)
    }
}

class FakeSettingsViewModel {

    private val _userSettings = kotlinx.coroutines.flow.MutableStateFlow(UserSettings())
    val userSettings: kotlinx.coroutines.flow.StateFlow<UserSettings> = _userSettings

    fun changeLanguage(lang: String, context: Context) {
        _userSettings.value = _userSettings.value.copy(language = lang)
    }

    fun changeFontType(font: String) {
        _userSettings.value = _userSettings.value.copy(fontType = font)
    }

    fun changeFontSize(size: Float) {
        _userSettings.value = _userSettings.value.copy(fontSize = size)
    }

    fun resetToDefault(context: Context) {
        _userSettings.value = UserSettings()
    }
}

