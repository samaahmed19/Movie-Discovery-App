@file:Suppress("UNCHECKED_CAST")

package com.example.movie_discovery.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_discovery.R
import com.example.movie_discovery.Viewmodels.SettingsViewModel
import com.example.movie_discovery.data.SettingsDataStore
import com.example.movie_discovery.ui.theme.AccentRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBackClick: () -> Unit = {} ) {
    val context = LocalContext.current
    val dataStore = remember { SettingsDataStore(context) }

    val settingsViewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(dataStore) as T
            }
        }
    )

    val selectedLanguage by settingsViewModel.selectedLanguage.collectAsState()
    val fontSize by settingsViewModel.fontSize.collectAsState()
    val fontType by settingsViewModel.fontType.collectAsState()

    val customFont = when (fontType) {
        "Roboto" -> FontFamily(Font(R.font.roboto_regular))
        "Cairo" -> FontFamily(Font(R.font.cairo_regular))
        else -> FontFamily(Font(R.font.momo_regular))
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (selectedLanguage == "ar") "الإعدادات" else "Settings",
                        fontWeight = FontWeight.Bold,
                        color = AccentRed,
                        fontFamily = customFont,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Language
            Text(
                text = if (selectedLanguage == "ar") "اللغة" else "Language",
                fontFamily = customFont,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize.sp
            )

            LanguageSelector(
                selectedLanguage = selectedLanguage,
                onLanguageChange = { lang -> settingsViewModel.changeLanguage(lang, context) },
                fontFamily = customFont,
                fontSize = fontSize.sp
            )

            Divider()

            // Font Type
            Text(
                text = if (selectedLanguage == "ar") "نوع الخط" else "Font Type",
                fontFamily = customFont,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize.sp
            )

            FontTypeSelector(
                selectedFont = fontType,
                onFontChange = { settingsViewModel.changeFontType(it) },
                fontFamily = customFont,
                fontSize = fontSize.sp
            )

            Divider()

            // Font Size
            Text(
                text = if (selectedLanguage == "ar") "حجم الخط" else "Font Size",
                fontFamily = customFont,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize.sp
            )

            FontSizeSlider(
                fontSize = fontSize,
                onFontSizeChange = { settingsViewModel.changeFontSize(it) },
                fontFamily = customFont
            )

            Divider()

            Button(
                onClick = { settingsViewModel.resetToDefault() },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
            ) {
                Text(
                    text = if (selectedLanguage == "ar") "إعادة الإعدادات الافتراضية" else "Reset to Default",
                    fontFamily = customFont,
                    fontSize = fontSize.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


// Language Selector

@Composable
fun LanguageSelector(
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    fontFamily: FontFamily,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LanguageOption("English", selectedLanguage == "en", { onLanguageChange("en") }, fontFamily, fontSize)
        LanguageOption("العربية", selectedLanguage == "ar", { onLanguageChange("ar") }, fontFamily, fontSize)
    }
}


// Language Option

@Composable
fun LanguageOption(
    language: String,
    selected: Boolean,
    onSelect: () -> Unit,
    fontFamily: FontFamily,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        color = if (selected) AccentRed.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
        tonalElevation = if (selected) 2.dp else 0.dp
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(language, fontFamily = fontFamily, fontSize = fontSize)
            RadioButton(selected = selected, onClick = onSelect)
        }
    }
}


// Font Type Selector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontTypeSelector(
    selectedFont: String,
    onFontChange: (String) -> Unit,
    fontFamily: FontFamily,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    var expanded by remember { mutableStateOf(false) }
    val fonts = listOf("Momo", "Roboto", "Cairo")

    Box {
        OutlinedTextField(
            value = selectedFont,
            onValueChange = {},
            readOnly = true,
            label = { Text("Font", fontFamily = fontFamily, fontSize = fontSize) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            trailingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    Modifier.clickable { expanded = !expanded })
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            fonts.forEach { font ->
                DropdownMenuItem(
                    text = { Text(font, fontFamily = fontFamily, fontSize = fontSize) },
                    onClick = {
                        onFontChange(font)
                        expanded = false
                    }
                )
            }
        }
    }
}


// Font Size Slider

@Composable
fun FontSizeSlider(
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    fontFamily: FontFamily
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Slider(
            value = fontSize,
            onValueChange = onFontSizeChange,
            valueRange = 12f..26f,
            steps = 7,
            colors = SliderDefaults.colors(
                thumbColor = AccentRed,
                activeTrackColor = AccentRed
            )
        )
        Text("${fontSize.toInt()} sp", fontFamily = fontFamily, fontSize = fontSize.sp)
    }
}




