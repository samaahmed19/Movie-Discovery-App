package com.example.movie_discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.movie_discovery.ui.theme.AccentRed
import com.example.movie_discovery.viewmodel.MovieDetailViewModel

@Composable
fun MovieDetailsScreen(
    movieId: Int?, // ✅ receives movie ID only
    viewModel: MovieDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var isFavorite by remember { mutableStateOf(false) }

    // Observe movie detail from ViewModel
    val movieDetail by viewModel.movieDetails.collectAsState()

    // Fetch movie details when ID changes
    LaunchedEffect(movieId) {
        movieId?.let { viewModel.getMovieDetails(it) }
    }

    // Loading indicator
    if (movieDetail == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AccentRed)
        }
    } else {
        // Screen content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Poster
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movieDetail?.poster_path}",
                    contentDescription = movieDetail?.title ?: "Movie Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                )

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) AccentRed else Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            Text(
                text = movieDetail?.title ?: "No title available",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AccentRed
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rating & Release
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Rating: ${movieDetail?.vote_average ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Release: ${movieDetail?.release_date ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Overview
            Text(
                text = movieDetail?.overview ?: "No description available",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.DarkGray,
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* TODO: Watch movie */ },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Watch",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Watch Now", color = Color.White)
                }

                OutlinedButton(
                    onClick = { /* TODO: Share movie */ },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Share")
                }
            }
        }
    }
}
