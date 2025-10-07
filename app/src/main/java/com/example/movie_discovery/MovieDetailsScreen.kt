package com.example.movie_discovery

import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
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
import coil.compose.AsyncImage
import com.example.movie_discovery.ui.theme.AccentRed
import com.example.movie_discovery.ui.theme.MoviesTheme

@Composable
fun MovieDetailsScreen(movie: Movie = getSampleMovies()[0]) {
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Poster
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) AccentRed else Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Rating
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFFD700)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${movie.rating} / 5",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Description
        Text(
            text = "A captivating story filled with suspense and drama. This is where a longer movie description will go when connected to the API.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.DarkGray,
                lineHeight = 22.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* TODO: watch action */ },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Watch",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Watch Now")
            }

            OutlinedButton(
                onClick = { /* TODO: share action */ },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Share")
            }
        }
    }
}

@Preview(showBackground = true, name = "Movie Details Light")
@Composable
fun MovieDetailsPreviewLight() {
    MoviesTheme(darkTheme = false) {
        MovieDetailsScreen()
    }
}

@Preview(showBackground = true, name = "Movie Details Dark")
@Composable
fun MovieDetailsPreviewDark() {
    MoviesTheme(darkTheme = true) {
        MovieDetailsScreen()
    }
}
