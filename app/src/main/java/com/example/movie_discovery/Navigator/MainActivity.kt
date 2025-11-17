package com.example.movie_discovery.Navigator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movie_discovery.Screens.CategoryScreen
import com.example.movie_discovery.Screens.HomeScreen
import com.example.movie_discovery.Screens.MovieDetailsScreen
import com.example.movie_discovery.Screens.Profile
import com.example.movie_discovery.Screens.SearchScreen
import com.example.movie_discovery.Screens.SettingsScreen
import com.example.movie_discovery.Screens.SignInScreen
import com.example.movie_discovery.Screens.SignUpScreen
import com.example.movie_discovery.Screens.SplashScreen
import com.example.movie_discovery.Screens.TrailerScreen
import com.example.movie_discovery.Viewmodels.SettingsViewModel
import com.example.movie_discovery.Viewmodels.ThemeViewModel
import com.example.movie_discovery.data.AuthViewModel
import com.example.movie_discovery.ui.theme.MoviesTheme
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val systemDark = isSystemInDarkTheme()

            LaunchedEffect(Unit) {
                themeViewModel.loadDarkMode(defaultDarkMode = systemDark)
            }

            val isDarkMode = themeViewModel.isDarkMode

            val context = this

            MoviesTheme(darkTheme = isDarkMode) {
                MyApp(themeViewModel = themeViewModel)

            }
        }
    }
}

@Composable
fun MyApp(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val systemDark = isSystemInDarkTheme()
    val settingsViewModel: SettingsViewModel = viewModel()

    LaunchedEffect(Unit) {
        themeViewModel.loadDarkMode(defaultDarkMode = systemDark)
    }

    val isDarkMode = themeViewModel.isDarkMode

    MoviesTheme(darkTheme = isDarkMode) {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            // ---------------------------
            // Splash Screen
            // ---------------------------
            composable("splash") {
                SplashScreen(
                    onTimeout = {
                        navController.navigate("signIn") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            // ---------------------------
            // Sign In Screen
            // ---------------------------
            composable("signIn") {
                SignInScreen(navController = navController)
            }

            // ---------------------------
            // Sign Up Screen
            // ---------------------------
            composable("signup") {
                SignUpScreen(navController = navController)
            }

            // ---------------------------
            // Home Screen
            // ---------------------------
            composable("home") {
                HomeScreen(
                    navController = navController,
                    onMovieClick = { movieId ->
                        navController.navigate("details/$movieId")
                    },
                    onSearchClick = {
                        navController.navigate("search_screen")
                    },
                    onProfileClick = {
                        navController.navigate("profile")
                    },
                    onSettingsClick = {
                        navController.navigate("settings")
                    }
                )
            }

            // ---------------------------
            // Search Screen
            // ---------------------------
            composable("search_screen") {
                SearchScreen(navController = navController)
            }

            // ---------------------------
            // Category Screen
            // ---------------------------
            composable(
                route = "category_screen/{genreId}/{genreName}",
                arguments = listOf(
                    navArgument("genreId") { type = NavType.IntType },
                    navArgument("genreName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val genreId = backStackEntry.arguments?.getInt("genreId") ?: 0
                val genreNameEncoded = backStackEntry.arguments?.getString("genreName") ?: ""
                val genreName =
                    URLDecoder.decode(genreNameEncoded, StandardCharsets.UTF_8.toString())
                CategoryScreen(
                    genreId = genreId,
                    genreName = genreName,
                    navController = navController
                )
            }

            // ---------------------------
            // Movie Details Screen
            // ---------------------------
            composable(
                route = "details/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId")
                MovieDetailsScreen(movieId = movieId, navController = navController)
            }

            // ---------------------------
            // Profile Screen
            // ---------------------------
            composable("profile") {
                Profile(
                    navController = navController,
                    userViewModel = viewModel(),
                    themeViewModel = themeViewModel
                )
            }
            // ---------------------------
            // Trailer Screen
            // ---------------------------
            composable(
                route = "trailer/{videoKey}/{movieId}",
                arguments = listOf(
                    navArgument("videoKey") { type = NavType.StringType },
                    navArgument("movieId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val videoKey = backStackEntry.arguments?.getString("videoKey")
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0

                TrailerScreen(
                    navController = navController,
                    videoKey = videoKey,
                    movieId = movieId
                )
            }
            // ---------------------------
            // Settings Screen
            // ---------------------------
            composable("settings") {
                SettingsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
