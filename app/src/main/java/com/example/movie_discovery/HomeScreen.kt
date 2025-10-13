package com.example.movie_discovery

import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.animation.AnimatedVisibility
import com.example.movie_discovery.ui.theme.MoviesTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movie_discovery.ui.theme.AccentRed

// -------------------------------
// Data Model
// -------------------------------
data class Movie(
    val id: Int,
    val title: String,
    val rating: Double,
    val posterUrl: String
)

// -------------------------------
// Home Screen
// -------------------------------
@Composable
fun HomeScreen(
    onMovieClick: (Int) -> Unit,  // Changed from (String) -> Unit to (Int) -> Unit
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // -------------------------------
        // Header with app title and icons
        // -------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Movie Discovery",
                style = MaterialTheme.typography.headlineMedium,
                color = AccentRed,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconButton(onClick = { /* TODO: Navigate to Profile Screen */ }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                IconButton(onClick = { /* TODO: Navigate to Settings */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Search bar, featured slider, tabs, and movie list
        SearchBar(onSearchClick = onSearchClick)
        FeaturedMoviesSlider()
        MovieTabs()
        MoviesList(movies = getSampleMovies(), onMovieClick = onMovieClick)
    }
}

// -------------------------------
// Search Bar
// -------------------------------
@Composable
fun SearchBar(onSearchClick: () -> Unit) {
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSearchClick() },
        placeholder = { Text("Search movies...") },
        singleLine = true,
        enabled = false
    )
}

// -------------------------------
// Featured Movies Slider
// -------------------------------
@Composable
fun FeaturedMoviesSlider() {
    val featuredImages = listOf(
        "https://image.tmdb.org/t/p/w500/ctMserH8g2SeOAnCw5gFjdQF8mo.jpg",
        "https://image.tmdb.org/t/p/w500/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg",
        "https://image.tmdb.org/t/p/w500/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg",
        "https://image.tmdb.org/t/p/w500/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg",
        "https://image.tmdb.org/t/p/w500/zpYf5JXfxybYyTfXYyZ9KJ8MGe1.jpg"
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(featuredImages) { image ->
            AsyncImage(
                model = image,
                contentDescription = "Featured Movie",
                modifier = Modifier
                    .width(280.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// -------------------------------
// Tabs Section
// -------------------------------
@Composable
fun MovieTabs() {
    val tabs = listOf("Popular", "Top Rated", "Upcoming")
    var selectedTab by remember { mutableStateOf(0) }

    ScrollableTabRow(selectedTabIndex = selectedTab) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { selectedTab = index },
                text = { Text(title) }
            )
        }
    }
}

// -------------------------------
// Movies List
// -------------------------------
@Composable
fun MoviesList(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(movies) { movie ->
            AnimatedMovieCard(movie = movie, onMovieClick = onMovieClick)
        }
    }
}

// -------------------------------
// Animated Movie Card
// -------------------------------
@Composable
fun AnimatedMovieCard(movie: Movie, onMovieClick: (Int) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(visible = visible) {
        MovieCard(movie = movie, onMovieClick = onMovieClick)
    }
}

// -------------------------------
// Movie Card
// -------------------------------
@Composable
fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit = {}
) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .width(160.dp)
            .height(260.dp)
            .clickable { onMovieClick(movie.id) }, // send movieId instead of title
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = movie.posterUrl,
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
                            text = "${movie.rating}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color.Red else Color.LightGray,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(24.dp)
                    .clickable { isFavorite = !isFavorite }
            )
        }
    }
}

// -------------------------------
// Sample Data
// -------------------------------
fun getSampleMovies(): List<Movie> {
    return listOf(
        Movie(1, "Pulp Fiction", 4.8, "https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg"),
        Movie(2, "The Lord of the Rings", 4.9, "https://image.tmdb.org/t/p/w500/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg"),
        Movie(3, "The Shawshank Redemption", 4.9, "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg"),
        Movie(4, "The Dark Knight", 4.8, "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg"),
        Movie(5, "Inception", 4.7, "https://image.tmdb.org/t/p/w500/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg"),
        Movie(6, "Interstellar", 4.8, "https://image.tmdb.org/t/p/w500/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg")
    )
}

// -------------------------------
// Previews
// -------------------------------
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun HomeScreenLightPreview() {
    MoviesTheme(darkTheme = false) {
        HomeScreen(onMovieClick = {}, onSearchClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun HomeScreenDarkPreview() {
    MoviesTheme(darkTheme = true) {
        HomeScreen(onMovieClick = {}, onSearchClick = {})
    }
}
