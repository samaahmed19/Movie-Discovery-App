package com.example.movie_discovery

import androidx.compose.ui.draw.shadow
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movie_discovery.ui.theme.AccentRed
import com.example.movie_discovery.ui.theme.Gold
import com.example.movie_discovery.ui.theme.MoviesTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.LaunchedEffect



data class Movie(
    val id: Int,
    val title: String,
    val rating: Double
)

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        Text(
            text = "Popular Movies",
            style = MaterialTheme.typography.headlineMedium,
            color = AccentRed,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        MoviesList(movies = getSampleMovies())
    }
}
@Composable
fun SearchBar() {
    androidx.compose.material3.TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        placeholder = { Text("Search movies...") },
        singleLine = true,
        enabled = false
    )
}
@Composable
fun FeaturedMoviesSlider() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text("FEATURED", color = Color.White)
            }
        }
    }
}


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


@Composable
fun MoviesList(movies: List<Movie>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            MovieCard(movie = movie)
        }
    }
}
@Composable
fun AnimatedMovieCard(movie: Movie) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(visible = visible) {
        MovieCard(movie)
    }
}

@Composable
fun MovieCard(movie: Movie) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(260.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    )
    {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Add to favorites",
                tint = AccentRed,
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            Box(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "POSTER",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Gold,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = movie.rating.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

fun getSampleMovies(): List<Movie> {
    return listOf(
        Movie(id = 1, title = "Pulp Fiction", rating = 4.8),
        Movie(id = 2, title = "The Lord of the Rings", rating = 4.9),
        Movie(id = 3, title = "The Shawshank Redemption", rating = 4.9),
        Movie(id = 4, title = "The Dark Knight", rating = 4.8),
        Movie(id = 5, title = "Inception", rating = 4.7),
        Movie(id = 6, title = "Interstellar", rating = 4.8)
    )
}


@Preview(showBackground = true, name = "Light Mode")
@Composable
fun HomeScreenLightPreview() {
    MoviesTheme(darkTheme = false) {
        HomeScreen()
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun HomeScreenDarkPreview() {
    MoviesTheme(darkTheme = true) {
        HomeScreen()
    }
}