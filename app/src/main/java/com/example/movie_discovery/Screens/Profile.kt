package com.example.movie_discovery.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.movie_discovery.Viewmodels.UserViewModel
import com.example.movie_discovery.data.MovieDetailsResponse

@Composable
fun Profile(
    userViewModel: UserViewModel = viewModel(),
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        userViewModel.loadUserData()
    }

    val userData by userViewModel.userData.collectAsState()

    val favouriteMovies by produceState(initialValue = emptyList<MovieDetailsResponse>(), userData) {
        value = userData?.favourites?.mapNotNull {
            userViewModel.getMovieDetailsFromTMDB(it.toInt())
        } ?: emptyList()
    }

    val watchlistMovies by produceState(initialValue = emptyList<MovieDetailsResponse>(), userData) {
        value = userData?.watchlist?.mapNotNull {
            userViewModel.getMovieDetailsFromTMDB(it.toInt())
        } ?: emptyList()
    }

    val watchedMovies by produceState(initialValue = emptyList<MovieDetailsResponse>(), userData) {
        value = userData?.watched?.mapNotNull {
            userViewModel.getMovieDetailsFromTMDB(it.toInt())
        } ?: emptyList()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        userData?.let { user ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Welcome, ${user.firstName?.ifEmpty { "Guest" } ?: "Guest"}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                DarkModeSwitch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeToggle
                )
            }

            Spacer(Modifier.height(24.dp))

            MovieListSection("Favourites", favouriteMovies, userViewModel)
            Spacer(Modifier.height(16.dp))
            MovieListSection("Watchlist", watchlistMovies, userViewModel)
            Spacer(Modifier.height(16.dp))
            MovieListSection("Watched", watchedMovies, userViewModel)
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading user data...")
        }
    }
}

@Composable
fun MovieListSection(
    title: String,
    movies: List<MovieDetailsResponse>,
    userViewModel: UserViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (movies.isEmpty()) {
            Text(
                text = "No movies yet",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(movies) { movie ->
                    MovieCard(
                        movie = movie,
                        userViewModel = userViewModel,
                    )
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: MovieDetailsResponse,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(false) }

    val userData by userViewModel.userData.collectAsState()

    LaunchedEffect(userData) {
        val movieIdStr = movie.id.toString()
        isFavorite = userData?.favourites?.contains(movieIdStr) == true
    }

    Card(
        modifier = modifier
            .width(160.dp)
            .height(260.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${movie.voteAverage ?: 0.0}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }


            IconButton(
                onClick = {
                    val movieIdStr = movie.id.toString()
                    if (isFavorite) userViewModel.removeFromFavourites(movieIdStr)
                    else userViewModel.addToFavourites(movieIdStr)
                    isFavorite = !isFavorite
                },
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.LightGray
                )
            }
        }
    }
}

@Composable
fun DarkModeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val trackColor = if (checked) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    else MaterialTheme.colorScheme.surfaceVariant
    val thumbColor = if (checked) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurfaceVariant
    val icon = if (checked) "🌙" else "☀️"

    Box(
        modifier = Modifier
            .width(60.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 4.dp, vertical = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = if (checked) 28.dp else 0.dp)
                .size(24.dp)
                .clip(RoundedCornerShape(50))
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
