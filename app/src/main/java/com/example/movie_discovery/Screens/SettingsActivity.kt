package com.example.movie_discovery.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie_discovery.Viewmodels.SettingsViewModel
import com.example.movie_discovery.ui.theme.AccentRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {}
) {

    val settingsViewModel: SettingsViewModel = viewModel()
    val selectedLanguage by settingsViewModel.selectedLanguage.collectAsState()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold, color = AccentRed) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Language",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            LanguageSelector(
                selectedLanguage = selectedLanguage,
                onLanguageChange = { settingsViewModel.changeLanguage(it) }
            )
        }
    }
}

@Composable
fun LanguageSelector(
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit
) {

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LanguageOption(
            language = "English",
            selected = selectedLanguage == "en",
            onSelect = { onLanguageChange("en") }
        )

        LanguageOption(
            language = "العربية",
            selected = selectedLanguage == "ar",
            onSelect = { onLanguageChange("ar") }
        )
    }
}

@Composable
fun LanguageOption(language: String, selected: Boolean, onSelect: () -> Unit) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        color = if (selected) AccentRed.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = if (selected) 2.dp else 0.dp
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(language)
            RadioButton(selected = selected, onClick = onSelect)
        }
    }
}


