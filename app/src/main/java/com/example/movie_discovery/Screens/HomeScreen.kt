package com.example.movie_discovery.Screens

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
import com.example.movie_discovery.data.MovieDetailsResponse
import com.example.movie_discovery.Viewmodels.HomeViewModel
import com.example.movie_discovery.data.MovieResponse
import androidx.lifecycle.viewmodel.compose.viewModel



// -------------------------------
// Home Screen
// -------------------------------
@Composable
fun HomeScreen(
    onMovieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit = {}
) {
    // ViewModel + state
    val viewModel: HomeViewModel = viewModel()
    val popularMovies by viewModel.popularMovies.collectAsState()
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val upcomingMovies by viewModel.upcomingMovies.collectAsState()
    val topRatedMovies by viewModel.topRatedMovies.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    // fetch when screen appears
    LaunchedEffect(Unit) {
        viewModel.loadMovies()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

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

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick =  { onProfileClick() } ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }


        SearchBar(onSearchClick = onSearchClick)

        FeaturedMoviesSlider(movies = trendingMovies)


        MovieTabs(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        when (selectedTab) {
            0 -> MoviesList(
                movies = popularMovies,
                onMovieClick = onMovieClick
            )
            1 -> MoviesList(
                movies = topRatedMovies,
                onMovieClick = onMovieClick
            )
            2 -> MoviesList(
                movies = upcomingMovies,
                onMovieClick = onMovieClick
            )
        }


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
// Slider Section
// -------------------------------

@Composable
fun FeaturedMoviesSlider(movies: List<MovieDetailsResponse>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(movies) { movie ->
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
                contentDescription = movie.title,
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
fun MovieTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
){
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
    movies: List<com.example.movie_discovery.data.MovieDetailsResponse>,
    onMovieClick: (Int) -> Unit
) {


    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            AnimatedMovieCard(movie = movie, onMovieClick = onMovieClick)



    }
    }
}

// -------------------------------
// Animated Movie Card
// -------------------------------
@Composable
fun AnimatedMovieCard(
    movie: com.example.movie_discovery.data.MovieDetailsResponse,
    onMovieClick: (Int) -> Unit
) {

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(visible = visible) {
        MovieCard(  movie = movie,
            onMovieClick = onMovieClick )
    }
}

// -------------------------------
// Movie Card
// -------------------------------
@Composable
fun MovieCard(
    movie: com.example.movie_discovery.data.MovieDetailsResponse,
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit = {}
) {

    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .width(160.dp)
            .height(260.dp)
            .clickable { onMovieClick(movie.id) },
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
            ) { AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
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
                            text = "${movie.vote_average ?: 0.0}",
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
// Previews
// -------------------------------
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun HomeScreenLightPreview() {
    MoviesTheme(darkTheme = false) {
        HomeScreen(onMovieClick = {},
            onSearchClick = {},
            onProfileClick = {})
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun HomeScreenDarkPreview() {
    MoviesTheme(darkTheme = true) {
        HomeScreen(onMovieClick = {},
            onSearchClick = {},
            onProfileClick = {})
    }
}
