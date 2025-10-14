package com.example.movie_discovery.Navigator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movie_discovery.Screens.HomeScreen
import com.example.movie_discovery.Screens.MovieDetailsScreen
import com.example.movie_discovery.Screens.ProfileScreen
import com.example.movie_discovery.Screens.SearchScreen
import com.example.movie_discovery.Screens.SignInScreen
import com.example.movie_discovery.Screens.SignUpScreen
import com.example.movie_discovery.Screens.SplashScreen
import com.example.movie_discovery.Screens.getSampleMovies

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
