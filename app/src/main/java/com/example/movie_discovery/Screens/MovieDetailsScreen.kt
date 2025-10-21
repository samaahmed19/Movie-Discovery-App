package com.example.movie_discovery.Screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.movie_discovery.Viewmodels.MovieDetailViewModel
import com.example.movie_discovery.Viewmodels.UserViewModel
import com.example.movie_discovery.ui.theme.AccentRed
import com.example.movie_discovery.ui.theme.MoviesTheme

@Composable
fun MovieDetailsScreen(
    movieId: Int?,
    viewModel: MovieDetailViewModel = viewModel()
) {
    val movieDetail by viewModel.movieDetails.collectAsState()
    val userViewModel: UserViewModel = viewModel()
    val userData by userViewModel.userData.collectAsState()

    LaunchedEffect(movieId) {
        movieId?.let { viewModel.getMovieDetails(it) }
    }

    if (movieDetail == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AccentRed)
        }
    } else {
        val movieIdStr = movieDetail!!.id.toString()
        val isFavorite = movieIdStr in (userData?.favourites ?: emptyList())

        val favColor by animateColorAsState(
            targetValue = if (isFavorite) Color.Red else Color.LightGray.copy(alpha = 0.6f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movieDetail?.posterPath}",
                    contentDescription = movieDetail?.title ?: "Movie Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                )

                IconButton(
                    onClick = {
                        if (isFavorite) userViewModel.removeFromFavourites(movieIdStr)
                        else userViewModel.addToFavourites(movieIdStr)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite",
                        tint = favColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = movieDetail?.title ?: "No title available",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                    text = "Rating: ${movieDetail?.voteAverage ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Release: ${movieDetail?.releaseDate ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = movieDetail?.overview ?: "No description available",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Watch",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Watch Now", color = MaterialTheme.colorScheme.onPrimary)
                }

                OutlinedButton(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Share", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun MovieDetailsLightPreview() {
    MoviesTheme(darkTheme = false) {
        MovieDetailsScreen(movieId = 1)
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun MovieDetailsDarkPreview() {
    MoviesTheme(darkTheme = true) {
        MovieDetailsScreen(movieId = 1)
    }
}
