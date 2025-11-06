package com.example.movie_discovery.ui.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.runtime.staticCompositionLocalOf

val LocalFontFamily = staticCompositionLocalOf<FontFamily> {
    error("No FontFamily provided")
}
@Composable
fun MoviesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val typography = Typography(
        bodyLarge = typography.bodyLarge.copy(fontFamily = LocalFontFamily.current),
        bodyMedium = typography.bodyMedium.copy(fontFamily = LocalFontFamily.current),
        bodySmall = typography.bodySmall.copy(fontFamily = LocalFontFamily.current),
        titleLarge = typography.titleLarge.copy(fontFamily = LocalFontFamily.current),
        titleMedium = typography.titleMedium.copy(fontFamily = LocalFontFamily.current),
        titleSmall = typography.titleSmall.copy(fontFamily = LocalFontFamily.current)
    )
    val DarkColorScheme = darkColorScheme(
        primary = AccentRed,
        secondary = Gold,
        background = DarkNavy,
        surface = CardBackground,
        onPrimary = TextPrimary,
        onSecondary = TextPrimary,
        onBackground = TextPrimary,
        onSurface = TextPrimary
    )

    val LightColorScheme = lightColorScheme(
        primary = AccentRed,
        secondary = Gold,
        background = androidx.compose.ui.graphics.Color.White,
        surface = androidx.compose.ui.graphics.Color(0xFFF5F5F5),
        onPrimary = androidx.compose.ui.graphics.Color.White,
        onSecondary = androidx.compose.ui.graphics.Color.Black,
        onBackground = androidx.compose.ui.graphics.Color.Black,
        onSurface = androidx.compose.ui.graphics.Color.Black
    )

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val animatedColorScheme = colorScheme.copy(
        background = animateColorAsState(colorScheme.background).value,
        surface = animateColorAsState(colorScheme.surface).value
    )

    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = typography,
        content = content
    )
}




