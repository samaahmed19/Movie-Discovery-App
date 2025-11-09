package com.example.movie_discovery.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.movie_discovery.R
import com.example.movie_discovery.Viewmodels.MovieDetailViewModel
import com.example.movie_discovery.Viewmodels.SettingsViewModel
import com.example.movie_discovery.Viewmodels.UserViewModel
import com.example.movie_discovery.ui.theme.AccentRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MovieDetailsScreen(
    movieId: Int?,
    viewModel: MovieDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {

    val movieDetail by viewModel.movieDetails.collectAsState()
    val userViewModel: UserViewModel = viewModel()


    var isFavorite by remember { mutableStateOf(false) }
    var isWatchlist by remember { mutableStateOf(false) }
    var isWatched by remember { mutableStateOf(false) }


    var favoriteScale by remember { mutableFloatStateOf(1f) }
    val coroutineScope = rememberCoroutineScope()


    val scrollState = rememberScrollState()
    val userSettings by settingsViewModel.userSettings.collectAsState()

    val selectedLanguage = userSettings.language
    val fontType = userSettings.fontType
    val fontSize = userSettings.fontSize

    val customFont = when (fontType) {
        "Roboto" -> FontFamily(Font(com.example.movie_discovery.R.font.roboto_regular))
        "Cairo" -> FontFamily(Font(com.example.movie_discovery.R.font.cairo_regular))
        else -> FontFamily(Font(R.font.momo_regular))
    }
    LaunchedEffect(Unit) {
        userViewModel.loadUserData()
    }


    LaunchedEffect(movieId, userViewModel.userData.collectAsState().value) {
        val userData = userViewModel.userData.value
        val movieIdStr = movieId?.toString()
        isFavorite = movieIdStr in (userData?.favourites ?: emptyList())
        isWatchlist = movieIdStr in (userData?.watchlist ?: emptyList())
        isWatched = movieIdStr in (userData?.watched ?: emptyList())
    }


    LaunchedEffect(movieId,selectedLanguage) {
        movieId?.let {
            val languageCode = if (selectedLanguage == "ar") "ar-SA" else "en-US"
            viewModel.getMovieDetails(it,languageCode) }
    }


    if (movieDetail == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AccentRed)
        }
    }

    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {

                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movieDetail?.posterPath}",
                    contentDescription = movieDetail?.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                )


                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .size(48.dp)
                        .scale(favoriteScale)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.25f))
                        .clickable {
                            movieId?.let {
                                if (isFavorite)
                                    userViewModel.removeFromFavourites(it.toString())
                                else
                                    userViewModel.addToFavourites(it.toString())
                                userViewModel.loadUserData()
                                isFavorite = !isFavorite

                                favoriteScale = 1.3f
                                coroutineScope.launch {
                                    delay(150)
                                    favoriteScale = 1f
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {


                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.LightGray,
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconBox(
                    icon = Icons.Filled.Star,
                    tint = if (isWatched) Color(0xFFFFD700) else Color.LightGray,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.25f), shape = CircleShape),
                    onClick = {
                        movieId?.let {
                            if (isWatched)
                                userViewModel.unmarkFromWatched(it.toString())
                            else
                                userViewModel.markAsWatched(it.toString())
                            userViewModel.loadUserData()
                            isWatched = !isWatched
                        }
                    }
                )

                IconBox(
                    icon = Icons.Filled.PlayArrow,
                    tint = if (isWatchlist) Color(0xFF00C853) else Color.LightGray,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.25f), shape = CircleShape),
                    onClick = {
                        movieId?.let {
                            if (isWatchlist)
                                userViewModel.removeFromWatchlist(it.toString())
                            else
                                userViewModel.addToWatchlist(it.toString())
                            userViewModel.loadUserData()
                            isWatchlist = !isWatchlist
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
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
                    text = if (selectedLanguage == "ar")
                        "التقييم: ${movieDetail?.voteAverage ?: "غير متاح"}"
                    else
                        "Rating: ${movieDetail?.voteAverage ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontFamily = customFont,
                        fontSize = fontSize.sp
                    )
                )


                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (selectedLanguage == "ar")
                        "تاريخ الإصدار: ${movieDetail?.releaseDate ?: "غير معروف"}"
                    else
                        "Release: ${movieDetail?.releaseDate ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontFamily = customFont,
                        fontSize = fontSize.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            Text(
                text = movieDetail?.overview
                    ?: if (selectedLanguage == "ar") "لا يوجد وصف متاح" else "No description available",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 22.sp,
                    fontFamily = customFont,
                    fontSize = fontSize.sp
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
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) {

                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Watch",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (selectedLanguage == "ar") "شاهد الآن" else "Watch Now",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = customFont,
                        fontSize = fontSize.sp
                    )

                }


                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (selectedLanguage == "ar") "مشاركة" else "Share",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = customFont,
                        fontSize = fontSize.sp
                    )

                }
            }
        }
    }
}

@Composable
fun IconBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(26.dp))
    }
}