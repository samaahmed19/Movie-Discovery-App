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
        // Splash Screen
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("signIn") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // Sign In Screen
        composable("signIn") {
            SignInScreen(navController = navController)
        }

        // Sign Up Screen
        composable("signUpScreen") {
            SignUpScreen(navController = navController)
        }

        // Home Screen
        composable("home") {
            HomeScreen(
                onMovieClick = { movieId ->
                    navController.navigate("details/$movieId")
                },
                onSearchClick = {
                    navController.navigate("search")
                }
            )
        }

        // Search Screen
        composable("search") {
            SearchScreen()
        }

        // Movie Details Screen (uses movieId instead of movieTitle)
        composable(
            route = "details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            MovieDetailsScreen(movieId = movieId)
        }
    }
}
