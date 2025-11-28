package com.example.movie_discovery.Screens

import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movie_discovery.R
import com.example.movie_discovery.Viewmodels.SettingsViewModel
import androidx.compose.foundation.lazy.itemsIndexed
import kotlinx.coroutines.delay
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun Profile(
    navController: NavController
) {
    val context: Context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()
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

    LaunchedEffect(Unit) {
        userViewModel.loadUserData()
    }
    val scrollState = rememberScrollState()

    val userData by userViewModel.userData.collectAsState()
    val favouriteMovies by userViewModel.favouriteMovies.collectAsState()
    val watchlistMovies by userViewModel.watchlistMovies.collectAsState()
    val watchedMovies by userViewModel.watchedMovies.collectAsState()

    var animateFavourites by remember { mutableStateOf(true) }
    var animateWatchlist by remember { mutableStateOf(true) }
    var animateWatched by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        userData?.let { user ->

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = if (selectedLanguage == "ar")
                        "مرحبًا، ${user.firstName?.ifEmpty { "زائر" } ?: "زائر"}"
                    else
                        "Hello, ${user.firstName?.ifEmpty { "Guest" } ?: "Guest"}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    fontFamily = customFont,
                    fontSize = fontSize.sp,
                    modifier = Modifier.align(Alignment.Center)
                )

            }

            Spacer(Modifier.height(24.dp))

            MovieListSection(
                title = if (selectedLanguage == "ar") "المفضلة" else "Favourites",
                movies = favouriteMovies,
                userViewModel = userViewModel,
                navController = navController,
                customFont = customFont,
                fontSize = fontSize.sp,
                selectedLanguage = selectedLanguage,
                animateOnFirstAppearance = animateFavourites
            )
            Spacer(Modifier.height(16.dp))
            MovieListSection(
                title = if (selectedLanguage == "ar") "قائمة المشاهدة" else "Watchlist",
                movies = watchlistMovies,
                userViewModel = userViewModel,
                navController = navController,
                customFont = customFont,
                fontSize = fontSize.sp,
                selectedLanguage = selectedLanguage,
                animateOnFirstAppearance = animateWatchlist
            )
            Spacer(Modifier.height(16.dp))
            MovieListSection(
                title = if (selectedLanguage == "ar") "تمت مشاهدتها" else "Watched",
                movies = watchedMovies,
                userViewModel = userViewModel,
                navController = navController,
                customFont = customFont,
                fontSize = fontSize.sp,
                selectedLanguage = selectedLanguage,
                animateOnFirstAppearance = animateWatched
            )
        } ?: Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (selectedLanguage == "ar") "جارٍ تحميل البيانات..." else "Loading user data...",
                fontFamily = customFont,
                fontSize = fontSize.sp,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
            )
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                userViewModel.logout {
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate("signIn") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = if (selectedLanguage == "ar") "تسجيل الخروج" else "Log Out",
                color = Color.White,
                fontFamily = customFont,
                fontSize = fontSize.sp
            )
        }
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                userViewModel.deleteAccount { success ->
                    Toast.makeText(context, if (success) "Account deleted" else "Failed to delete account", Toast.LENGTH_SHORT).show()
                    if (success) navController.navigate("signIn") { popUpTo(0) { inclusive = true } }
                }

            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(
                text = if (selectedLanguage == "ar") "حذف الحساب" else "Delete Account",
                color = Color.White,
                fontFamily = customFont,
                fontSize = fontSize.sp
            )
        }
    }
    LaunchedEffect(favouriteMovies) {
        if (animateFavourites && favouriteMovies.isNotEmpty()) {
            delay(1000)
            animateFavourites = false
        }
    }

    LaunchedEffect(watchlistMovies) {
        if (animateWatchlist && watchlistMovies.isNotEmpty()) {
            delay(1000)
            animateWatchlist = false
        }
    }

    LaunchedEffect(watchedMovies) {
        if (animateWatched && watchedMovies.isNotEmpty()) {
            delay(1000)
            animateWatched = false
        }
    }
}

@Composable
fun MovieListSection(
    title: String,
    movies: List<MovieDetailsResponse>,
    userViewModel: UserViewModel,
    navController: NavController,
    customFont: FontFamily,
    fontSize: androidx.compose.ui.unit.TextUnit,
    selectedLanguage: String,
    animateOnFirstAppearance: Boolean
) {
    var listAnimated by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = movies.isNotEmpty()) {
        if (movies.isNotEmpty() && animateOnFirstAppearance) {
            delay(150)
            listAnimated = true
        }
    }

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
                text = if (selectedLanguage == "ar") "لا توجد أفلام بعد" else "No movies yet",
                fontFamily = customFont,
                fontSize = fontSize,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(items = movies, key = { _, movie -> movie.id }) { index, movie ->
                    if (animateOnFirstAppearance) {
                        AnimatedProfileMovieCard(
                            movie = movie,
                            userViewModel = userViewModel,
                            onMovieClick = { id -> navController.navigate("details/$id") },
                            index = index,
                            animate = listAnimated
                        )
                    } else {
                        ProfileMovieCard(
                            movie = movie,
                            userViewModel = userViewModel,
                            onMovieClick = { id -> navController.navigate("details/$id") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedProfileMovieCard(
    movie: MovieDetailsResponse,
    userViewModel: UserViewModel,
    onMovieClick: (Int) -> Unit,
    index: Int,
    animate: Boolean
) {
    val targetProgress = if (animate) 1f else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = index * 100,
            easing = EaseOutCubic
        )
    )

    ProfileMovieCard(
        movie = movie,
        userViewModel = userViewModel,
        onMovieClick = onMovieClick,
        modifier = Modifier
            .graphicsLayer {
                alpha = animatedProgress
                translationX = (-100 * (1 - animatedProgress))
                translationY = (-30 * (1 - animatedProgress))
            }
    )
}
@Composable
fun ProfileMovieCard(
    movie: MovieDetailsResponse,
    userViewModel: UserViewModel,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val userData by userViewModel.userData.collectAsState()
    val isFavorite = userData?.favourites?.contains(movie.id.toString()) == true

    Card(
        modifier = modifier
            .width(160.dp)
            .height(260.dp)
            .clickable { onMovieClick(movie.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
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
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${movie.voteAverage ?: 0.0}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            IconButton(
                onClick = {
                    val movieIdStr = movie.id.toString()
                    if (isFavorite) userViewModel.removeFromFavourites(movieIdStr)
                    else userViewModel.addToFavourites(movieIdStr)
                },
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.White.copy(
                        alpha = 0.8f
                    )
                )
            }
        }
    }
}
