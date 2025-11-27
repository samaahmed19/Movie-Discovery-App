package com.example.movie_discovery.Screens

import androidx.compose.material.icons.filled.Person
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.movie_discovery.data.MovieDetailsResponse
import com.example.movie_discovery.Viewmodels.HomeViewModel
import com.example.movie_discovery.Viewmodels.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movie_discovery.R
import com.example.movie_discovery.Viewmodels.SettingsViewModel
import com.example.movie_discovery.Viewmodels.ThemeViewModel

// -------------------------------
// Home Screen
// -------------------------------
@Composable
fun HomeScreen(
    navController: NavController,
    userId: String?,
    themeViewModel: ThemeViewModel,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
)  {
    val settingsViewModel: SettingsViewModel = viewModel()
    val viewModel: HomeViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    val popularMovies by viewModel.popularMovies.collectAsState()
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val upcomingMovies by viewModel.upcomingMovies.collectAsState()
    val topRatedMovies by viewModel.topRatedMovies.collectAsState()
    val userSettings by settingsViewModel.userSettings.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }

    val selectedLanguage = userSettings.language
    val fontType = userSettings.fontType
    val fontSize = userSettings.fontSize

    val customFont = when (fontType) {
        "Roboto" -> FontFamily(Font(com.example.movie_discovery.R.font.roboto_regular))
        "Cairo" -> FontFamily(Font(com.example.movie_discovery.R.font.cairo_regular))
        else -> FontFamily(Font(R.font.momo_regular))
    }
    val systemDark = isSystemInDarkTheme()

    LaunchedEffect(userId) {
        themeViewModel.loadDarkMode(defaultDarkMode = systemDark )
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
                text = if (selectedLanguage == "ar") "اكتشف الأفلام" else "Movie Discovery",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontFamily = customFont,
                fontSize = fontSize.sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // settings icon
                IconButton(onClick = { onSettingsClick() }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                // Profile icon
                IconButton(onClick = { onProfileClick() }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        SearchBar(onSearchClick = onSearchClick)
        FeaturedMoviesSlider(
            movies = trendingMovies,
            userViewModel = userViewModel,
            navController = navController
        )

        MovieTabs(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        when (selectedTab) {
            0 -> MoviesList(movies = popularMovies, navController = navController, userViewModel = userViewModel)
            1 -> MoviesList(movies = topRatedMovies, navController = navController, userViewModel = userViewModel)
            2 -> MoviesList(movies = upcomingMovies, navController = navController, userViewModel = userViewModel)
        }
    }
}

// -------------------------------
// Search Bar
// -------------------------------
@Composable
fun SearchBar(onSearchClick: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel()
    val userSettings by settingsViewModel.userSettings.collectAsState()

    val selectedLanguage = userSettings.language
    val fontType = userSettings.fontType
    val fontSize = userSettings.fontSize

    val customFont = when (fontType) {
        "Roboto" -> FontFamily(Font(R.font.roboto_regular))
        "Cairo" -> FontFamily(Font(R.font.cairo_regular))
        else -> FontFamily(Font(R.font.momo_regular))
    }

    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onSearchClick() },
        placeholder = {
            Text(
                text = if (selectedLanguage == "ar") "ابحث عن فيلم..." else "Search movies...",
                fontFamily = customFont,
                fontSize = fontSize.sp,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f)
            )
        },
        singleLine = true,
        enabled = false
    )
}

// -------------------------------
// Slider Section
// -------------------------------
@Composable
fun FeaturedMoviesSlider(
    movies: List<MovieDetailsResponse>,
    userViewModel: UserViewModel,
    navController: NavController
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            AnimatedFeaturedHomeCard(
                movie = movie,
                userViewModel = userViewModel
            ) {
                navController.navigate("details/${movie.id}")
            }
        }
    }
}

@Composable
fun AnimatedFeaturedHomeCard(
    movie: MovieDetailsResponse,
    userViewModel: UserViewModel,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it / 2 }),
        exit = fadeOut()
    ) {
        FeaturedHomeCard(
            movie = movie,
            userViewModel = userViewModel,
            onClick = onClick
        )
    }
}


@Composable
fun FeaturedHomeCard(
    movie: MovieDetailsResponse,
    userViewModel: UserViewModel,
    onClick: () -> Unit
) {
    val userData by userViewModel.userData.collectAsState()
    val isFavorite = movie.id.toString() in (userData?.favourites ?: emptyList())

    Card(
        modifier = Modifier
            .width(240.dp)
            .aspectRatio(16 / 9f)
            .clickable(onClick = onClick)
            .shadow(10.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.backdropPath ?: movie.posterPath}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = {
                    if (isFavorite)
                        userViewModel.removeFromFavourites(movie.id.toString())
                    else
                        userViewModel.addToFavourites(movie.id.toString())
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.8f)
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = movie.title ?: "Unknown",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint =MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${movie.voteAverage ?: 0.0}",
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

// -------------------------------
// Tabs Section
// -------------------------------
@Composable
fun MovieTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel()
    val userSettings by settingsViewModel.userSettings.collectAsState()
    val selectedLanguage = userSettings.language
    val fontType = userSettings.fontType
    val fontSize = userSettings.fontSize

    val customFont = when (fontType) {
        "Roboto" -> FontFamily(Font(R.font.roboto_regular))
        "Cairo" -> FontFamily(Font(R.font.cairo_regular))
        else -> FontFamily(Font(R.font.momo_regular))
    }

    val tabs = listOf("Popular", "Top Rated", "Upcoming")
    ScrollableTabRow(selectedTabIndex = selectedTab , containerColor = MaterialTheme.colorScheme.surface) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = if (selectedLanguage == "ar") when (title) {
                            "Popular" -> "شائع"
                            "Top Rated" -> "الأعلى تقييماً"
                            "Upcoming" -> "قادمة قريباً"
                            else -> title
                        } else title,
                        fontFamily = customFont,
                        fontSize = fontSize.sp
                    )
                }
            )
        }
    }
}

// -------------------------------
// Movies List
// -------------------------------
@Composable
fun MoviesList(
    movies: List<MovieDetailsResponse>,
    navController: NavController,
    userViewModel: UserViewModel
) {
    var listAnimated by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = movies) {
        if (movies.isNotEmpty()) {
            listAnimated = true
        }
    }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        itemsIndexed(items = movies, key = { _, movie -> movie.id }) { index, movie ->

            val animatedProgress by animateFloatAsState(
                targetValue = if (listAnimated) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 400,
                    delayMillis = index * 100,
                    easing = EaseOutCubic
                )
            )
            MovieCard(
                movie = movie,
                userViewModel = userViewModel,
                onMovieClick = { navController.navigate("details/${movie.id}") },
                modifier = Modifier
                    .graphicsLayer {
                        alpha = animatedProgress
                        translationX = (-100 * (1 - animatedProgress))
                        translationY = (-30 * (1 - animatedProgress))
                    }
            )
        }
    }
}
// -------------------------------
// Movie Card
// -------------------------------
@Composable
fun MovieCard(
    movie: MovieDetailsResponse,
    modifier: Modifier = Modifier,
    onMovieClick: (Int) -> Unit = {},
    userViewModel: UserViewModel = viewModel()
) {
    var isFavorite by remember { mutableStateOf(false) }
    val userDataState = userViewModel.userData.collectAsState().value

    LaunchedEffect(userDataState) {
        val movieIdStr = movie.id.toString()
        isFavorite = movieIdStr in (userDataState?.favourites ?: emptyList())
    }

    Card(
        modifier = modifier
            .width(160.dp)
            .height(260.dp)
            .clickable { onMovieClick(movie.id) },
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
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${movie.voteAverage ?: 0.0}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.7f)
                        )
                    }
                }
            }

            // Favorite Icon
            IconButton(
                onClick = {
                    val movieIdStr = movie.id.toString()
                    if (isFavorite) userViewModel.removeFromFavourites(movieIdStr)
                    else userViewModel.addToFavourites(movieIdStr)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}
