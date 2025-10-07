package com.example.movie_discovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

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
                onMovieClick = { movieTitle ->
                    navController.navigate("details/$movieTitle")
                },
                onSearchClick = {
                    navController.navigate("search")
                },
                onProfileClick = {
                    navController.navigate("profile")
                }

            )
        }

        // ---------------------------
        // Search Screen
        // ---------------------------
        composable("search") {
            SearchScreen()
        }
        // ---------------------------
        // profile Screen
        // ---------------------------
        composable("profile") {
            ProfileScreen()
        }
        // ---------------------------
        // Movie Details Screen
        // ---------------------------
        composable(
            route = "details/{movieTitle}",
            arguments = listOf(navArgument("movieTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieTitle = backStackEntry.arguments?.getString("movieTitle")
            val movie = getSampleMovies().find { it.title == movieTitle }
            MovieDetailsScreen(movie = movie ?: getSampleMovies()[0])
        }
    }
}
