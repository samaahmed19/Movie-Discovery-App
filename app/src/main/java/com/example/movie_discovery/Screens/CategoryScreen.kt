package com.example.movie_discovery.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movie_discovery.Viewmodels.SearchViewModel
import com.example.movie_discovery.data.Movie
import kotlin.text.chunked
import kotlin.text.iterator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    genreId: Int,
    genreName: String,
    navController: NavHostController,
    viewModel: SearchViewModel = viewModel()
) {
    val movies by viewModel.moviesByGenre.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(key1 = genreId) {
        viewModel.getMoviesByGenre(genreId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = genreName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    val featuredMovies = movies.take(10)
                    if (featuredMovies.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Top Results in $genreName",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(featuredMovies) { movie ->
                                FeaturedCategoryCard(movie = movie) {
                                    navController.navigate("movie_detail_screen/${movie.id}")
                                }
                            }
                        }
                    }
                }

                item {
                    if (movies.isNotEmpty()) {
                        Text(
                            text = "All Movies",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                val gridRows = movies.chunked(3)
                items(gridRows) { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (movie in rowItems) {
                            Box(modifier = Modifier.weight(1f)) {
                                com.example.movie_discovery.Screens.MovieCardR(movie = movie) {
                                    navController.navigate("movie_detail_screen/${movie.id}")
                                }
                            }
                        }
                        repeat(3 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedCategoryCard(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .aspectRatio(16 / 9f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.backdropPath ?: movie.poster_path}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
@Composable
fun MovieCardR(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(2 / 3f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
