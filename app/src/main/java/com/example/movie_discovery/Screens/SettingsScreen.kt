package com.example.movie_discovery.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_discovery.R
import com.example.movie_discovery.Viewmodels.SettingsViewModel
import com.example.movie_discovery.Viewmodels.ThemeViewModel
import com.example.movie_discovery.ui.theme.AccentRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    themeViewModel : ThemeViewModel
) {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel()
    val userSettings by settingsViewModel.userSettings.collectAsState()
    val systemDark = isSystemInDarkTheme()

    LaunchedEffect(Unit) {
        themeViewModel.loadDarkMode( defaultDarkMode = systemDark)
    }

    val selectedLanguage = userSettings.language
    val fontType = userSettings.fontType
    val fontSize = userSettings.fontSize

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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    text = if (selectedLanguage == "ar") "الوضع الداكن" else "Dark Mode",
                    fontFamily = customFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize.sp
                )

                DarkModeSwitch(
                    themeViewModel = themeViewModel
                )
            }

            Divider()

            // Language
            Text(
                text = if (selectedLanguage == "ar") "اللغة" else "Language",
                fontFamily = customFont,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize.sp
            )

            LanguageSelector(
                selectedLanguage = selectedLanguage,
                onLanguageChange = { settingsViewModel.changeLanguage(it, context) },
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
                onClick = { settingsViewModel.resetToDefault(context) },
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

//Font Type Selector
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontTypeSelector(
    selectedFont: String,
    onFontChange: (String) -> Unit,
    fontFamily: FontFamily,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    var expanded by remember { mutableStateOf(false) }
    val fonts = listOf("Roboto", "Cairo", "Momo")

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

//Font Size Slider
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

@Composable
fun DarkModeSwitch(
    themeViewModel: ThemeViewModel = viewModel()
) {
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()

    val trackColor = if (isDarkMode) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    else MaterialTheme.colorScheme.surfaceVariant

    val thumbColor = if (isDarkMode) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurfaceVariant

    val icon = if (isDarkMode) "🌙" else "☀️"

    Box(
        modifier = Modifier
            .width(60.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
            .clickable { themeViewModel.toggleDarkMode() }
            .padding(horizontal = 4.dp, vertical = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = if (isDarkMode) 28.dp else 0.dp)
                .size(24.dp)
                .clip(RoundedCornerShape(50))
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, style = MaterialTheme.typography.bodyMedium)
        }
    }
}