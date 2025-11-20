package com.example.movie_discovery.Screens

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import android.widget.MediaController
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.movie_discovery.R
import com.example.movie_discovery.Viewmodels.SearchViewModel
import com.example.movie_discovery.Viewmodels.SettingsViewModel
import com.example.movie_discovery.Viewmodels.UserViewModel
import com.example.movie_discovery.data.Category
import com.example.movie_discovery.data.sampleCategories
import com.example.movie_discovery.ui.theme.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val recognizer = remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }
    val context = LocalContext.current
    val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var scannedText by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val userData by userViewModel.userData.collectAsState()
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
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    LaunchedEffect(query) {
        kotlinx.coroutines.delay(500)
        if (query.isNotBlank() && query.trim() != ".") {
            viewModel.searchMovies(query)
        } else {
            viewModel.clearSearchResults()
        }
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            processImage(InputImage.fromBitmap(bitmap, 0), context, recognizer, viewModel) { text ->
                query = text
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {

            showImageSourceDialog = false
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                processImage(InputImage.fromFilePath(context, uri), context, recognizer, viewModel) { text ->
                    query = text
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showImageSourceDialog = false
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    if (showImageSourceDialog) {
        ModalBottomSheet(
            onDismissRequest = { showImageSourceDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = if (selectedLanguage == "ar") "اختر طريقة المسح" else "Choose Scan Method",
                    fontFamily = customFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = (fontSize + 2).sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = {
                                if (androidx.core.content.ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.CAMERA
                                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                ) {
                                    showImageSourceDialog = false
                                    cameraLauncher.launch(null)
                                } else {
                                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                }
                            },
                            shape = CircleShape,
                            modifier = Modifier.size(72.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentRed,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                Icons.Default.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (selectedLanguage == "ar") "كاميرا" else "Camera",
                            fontFamily = customFont,
                            fontSize = fontSize.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = {
                                val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                    android.Manifest.permission.READ_MEDIA_IMAGES
                                } else {
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                                }

                                if (androidx.core.content.ContextCompat.checkSelfPermission(
                                        context,
                                        permission
                                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                ) {
                                    showImageSourceDialog = false
                                    galleryLauncher.launch("image/*")
                                } else {
                                    galleryPermissionLauncher.launch(permission)
                                }
                            },
                            shape = CircleShape,
                            modifier = Modifier.size(72.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentRed,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (selectedLanguage == "ar") "معرض" else "Gallery",
                            fontFamily = customFont,
                            fontSize = fontSize.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(
                            text = if (selectedLanguage == "ar") "ابحث عن فيلم..." else "Search movie...",
                            fontFamily = customFont,
                            fontSize = fontSize.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    },
                    trailingIcon = {
                        Row {
                            IconButton(onClick = { showImageSourceDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Scan",
                                    tint = TextSecondary
                                )
                            }
                            if (query.isNotEmpty()) {
                                IconButton(onClick = {
                                    query = ""
                                    viewModel.clearSearchResults()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50.dp))
                        .focusRequester(focusRequester),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = true
                )
            }
        }
    ) { paddingValues ->

        if (query.isBlank()) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = if (selectedLanguage == "ar") "اكتشف التصنيفات" else "Explore Categories",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = customFont,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(sampleCategories) { category ->
                        CategoryCard(
                            category = category,
                            onClick = {
                                val displayName =
                                    if (selectedLanguage == "ar") category.nameAr else category.nameEn
                                val encodedName = URLEncoder.encode(
                                    displayName,
                                    StandardCharsets.UTF_8.toString()
                                )
                                navController.navigate("category_screen/${category.id}/$encodedName")
                            },
                            selectedLanguage = selectedLanguage,
                            fontFamily = customFont,
                            fontSize = fontSize
                        )

                    }
                }
            }
        } else {

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (searchResults.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (selectedLanguage == "ar") "لا توجد نتائج لـ \"$query\"" else "No results found for \"$query\"",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = customFont,
                            fontSize = fontSize.sp
                        )
                    }
                }
            }
            else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(120.dp),
                    modifier = Modifier.padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(searchResults) { movie ->
                        Box(
                            modifier = Modifier
                                .width(180.dp)
                                .height(260.dp)
                        ) {
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) { visible = true }

                            val isFavorite =
                                movie.id.toString() in (userData?.favourites ?: emptyList())

                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { navController.navigate("details/${movie.id}") }
                                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(6.dp)
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
                                                .clip(
                                                    RoundedCornerShape(
                                                        topStart = 16.dp,
                                                        topEnd = 16.dp
                                                    )
                                                ),
                                            contentScale = ContentScale.Crop
                                        )

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 8.dp, vertical = 6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = movie.title ?: "Unknown",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
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
                                            if (isFavorite)
                                                userViewModel.removeFromFavourites(movie.id.toString())
                                            else
                                                userViewModel.addToFavourites(movie.id.toString())
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(10.dp)
                                            .size(24.dp)
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
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit,
    selectedLanguage: String,
    fontFamily: FontFamily,
    fontSize: Float
) {

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() }
            .shadow(10.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = if (selectedLanguage == "ar") category.nameAr else category.nameEn,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(vertical = 6.dp)
            ) {
                val displayName = if (selectedLanguage == "ar") category.nameAr else category.nameEn

                Text(
                    text = displayName,
                    color = Color.White,
                    fontFamily = fontFamily,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

            }

        }
    }
}

@Preview(showBackground = true, name = "Search Screen Preview")
@Composable
fun PreviewSearchScreen() {
    MoviesTheme(darkTheme = false) {
        val navController = rememberNavController()
        SearchScreen(navController = navController)
    }
}

fun processImage(
    image: InputImage,
    context: android.content.Context,
    recognizer: com.google.mlkit.vision.text.TextRecognizer,
    viewModel: SearchViewModel,
    onTextFound: (String) -> Unit
) {
    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            val blocks = visionText.textBlocks

            if (blocks.isEmpty()) {
                Toast.makeText(context, "No text found", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }
            val biggestBlock = blocks.maxByOrNull { block ->
                val height = block.boundingBox?.height() ?: 0
                val width = block.boundingBox?.width() ?: 0
                height
            }

            val bestText = biggestBlock?.text?.trim()

            val cleanText = bestText?.replace("\n", " ") ?: ""

            if (cleanText.isNotEmpty()) {
                Toast.makeText(context, "Searching for: $cleanText", Toast.LENGTH_SHORT).show()

                onTextFound(cleanText)
                viewModel.searchMovies(cleanText)
            } else {
                Toast.makeText(context, "Could not identify movie title", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}
