package com.example.movie_discovery.Screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movie_discovery.R
import com.example.movie_discovery.Viewmodels.SettingsViewModel
import com.example.movie_discovery.Viewmodels.TrailerViewModel
import com.example.movie_discovery.Viewmodels.UserViewModel
import com.example.movie_discovery.data.CastMember
import com.example.movie_discovery.ui.theme.AccentRed

@Composable
fun TrailerNeonText(text: String, neonColor: Color, textColor: Color) {
    val baseStyle = LocalTextStyle.current.copy(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic
    )
    Box {
        Text(
            text = text,
            color = Color.Transparent,
            style = baseStyle.copy(
                shadow = Shadow(
                    color = neonColor.copy(alpha = 0.5f),
                    offset = Offset(0f, 0f),
                    blurRadius = 15f
                )
            ),
            textAlign = TextAlign.Center
        )
        Text(text = text, color = textColor, style = baseStyle, textAlign = TextAlign.Center)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TrailerScreen(
    navController: NavController,
    videoKey: String?,
    movieId: Int,
    settingsViewModel: SettingsViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    trailerViewModel: TrailerViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        userViewModel.loadUserData()
    }

    val userSettings by settingsViewModel.userSettings.collectAsState()
    val selectedLanguage = userSettings.language
    val fontType = userSettings.fontType
    val fontSize = userSettings.fontSize

    LaunchedEffect(movieId) {
        trailerViewModel.getMovieCast(movieId, selectedLanguage)
    }
    val castList by trailerViewModel.cast.collectAsState()

    val userData by userViewModel.userData.collectAsState()
    val isDarkMode = userData?.isDarkMode == true

    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onBackground
    val iconTint = MaterialTheme.colorScheme.onBackground

    val customFont = when (fontType) {
        "Roboto" -> FontFamily(Font(R.font.roboto_regular))
        "Cairo" -> FontFamily(Font(R.font.cairo_regular))
        else -> FontFamily(Font(R.font.momo_regular))
    }

    var isFullScreen by remember { mutableStateOf(false) }
    var customView by remember { mutableStateOf<View?>(null) }

    val layoutDirection = if (selectedLanguage == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
    val youtubeLang = if (selectedLanguage == "ar") "ar" else "en"
    val videoUrl = "https://www.youtube.com/embed/$videoKey?autoplay=1&modestbranding=1&controls=1&fs=1&hl=$youtubeLang"

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(backgroundColor, surfaceColor)
    )

    BackHandler(enabled = isFullScreen) {
        isFullScreen = false
        customView = null
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    if (isFullScreen && customView != null) {
        AndroidView(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            factory = {
                FrameLayout(it).apply {
                    addView(customView)
                }
            }
        )
    } else {
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundBrush)
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 8.dp, start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = iconTint,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 45.dp)
                    ) {
                        TrailerNeonText(
                            text = "Movie",
                            neonColor = Color.Cyan.copy(alpha = 0.9f),
                            textColor = textColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        TrailerNeonText(
                            text = "Discovery",
                            neonColor = AccentRed.copy(alpha = 0.8f),
                            textColor = textColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                if (videoKey != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                WebView(context).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    settings.loadWithOverviewMode = true
                                    settings.useWideViewPort = true
                                    settings.pluginState = WebSettings.PluginState.ON

                                    webChromeClient = object : WebChromeClient() {
                                        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                                            super.onShowCustomView(view, callback)
                                            customView = view
                                            isFullScreen = true
                                            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                                        }

                                        override fun onHideCustomView() {
                                            super.onHideCustomView()
                                            customView = null
                                            isFullScreen = false
                                            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                        }
                                    }
                                    webViewClient = WebViewClient()
                                    loadUrl(videoUrl)
                                }
                            }
                        )

                        IconButton(
                            onClick = {
                                isFullScreen = true
                                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = "Fullscreen",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (selectedLanguage == "ar") "الفيديو غير متاح" else "Video unavailable",
                            color = textColor,
                            fontFamily = customFont,
                            fontSize = fontSize.sp
                        )
                    }
                }

                if (castList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = if (selectedLanguage == "ar") "طاقم التمثيل" else "Cast & Crew",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = customFont,
                        fontSize = fontSize.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(castList) { actor ->
                            AnimatedCastMemberItem(actor, textColor, customFont)
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun AnimatedCastMemberItem(actor: CastMember, textColor: Color, customFont: FontFamily) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it / 2 }),
        exit = fadeOut()
    ) {
        CastMemberItem(actor, textColor, customFont)
    }
}

@Composable
fun CastMemberItem(actor: CastMember, textColor: Color, customFont: FontFamily) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(85.dp)
    ) {
        Card(
            shape = CircleShape,
            border = BorderStroke(2.dp, AccentRed.copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.size(80.dp)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w200${actor.profilePath}",
                contentDescription = actor.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = actor.name,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            fontFamily = customFont,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Text(
            text = actor.character,
            style = MaterialTheme.typography.labelSmall,
            color = textColor.copy(alpha = 0.7f),
            fontFamily = customFont,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )
    }
}