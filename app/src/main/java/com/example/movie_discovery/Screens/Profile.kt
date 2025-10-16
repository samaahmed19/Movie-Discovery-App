package com.example.movie_discovery.Screens


import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie_discovery.ui.theme.MoviesTheme

data class MovieList(val name: String, val movies: List<String> = emptyList())

@Composable
fun Profile(
    isDarkMode: Boolean = false,
    onDarkModeToggle: (Boolean) -> Unit = {}
)  {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Username",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                )
                Spacer(modifier = Modifier.width(128.dp))
                DarkModeSwitch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeToggle
                )
            }

        }

        Divider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 12.dp)
        )


        Spacer(modifier = Modifier.height(32.dp))

        // replace with real data
        val movieLists = listOf(
            MovieList("Watched", listOf("Movie1", "Movie2", "Movie3")),
            MovieList("Watchlist", emptyList()),
            MovieList("Favourites", listOf("Movie4"))
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(movieLists) { list ->
                MovieList(list)
            }
        }

    }
}

@Composable
fun MovieList(MList: MovieList) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = MList.name,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (MList.movies.isEmpty()) {
            Text(
                text = "No movies yet",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(MList.movies) { movie ->
                    MovieCard(movie)
                }
            }
        }
    }
}

@Composable
fun MovieCard(movieName: String) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .clickable { /* navigate to movie details */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = movieName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}


@Composable
fun DarkModeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val transition = updateTransition(targetState = checked, label = "DarkModeTransition")

    val thumbOffset by transition.animateDp(label = "ThumbOffset") { state ->
        if (state) 28.dp else 0.dp
    }

    val trackColor by transition.animateColor(label = "TrackColor") { state ->
        if (state) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        else MaterialTheme.colorScheme.surfaceVariant
    }
    val thumbColor by transition.animateColor(label = "ThumbColor") { state ->
        if (state) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant
    }

    val icon = if (checked) "🌙" else "☀️"

    Box(
        modifier = Modifier
            .width(60.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 4.dp, vertical = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(24.dp)
                .clip(RoundedCornerShape(50))
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun ProfileLightPreview() {
    MoviesTheme(darkTheme = false) {
        Profile()
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun ProfileDarkPreview() {
    MoviesTheme(darkTheme = true) {
        Profile()
    }
}





