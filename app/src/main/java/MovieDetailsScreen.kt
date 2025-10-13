package com.example.movie_discovery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MovieDetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Movie Poster
        Image(
            painter = painterResource(id = R.drawable.placeholder_poster),
            contentDescription = "Movie Poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        // Movie Title
        Text(
            text = "Movie Title Is Here",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Rating + Release
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "⭐ Rating: 8.5/10",
                color = Color(0xFFFFC107),
                fontSize = 16.sp
            )
            Text(
                text = "📅 Release: 2024",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            text = "This is the description of the movie. It gives details about the story, actors, and other info. Right now it’s static text, later it will come from the API.",
            fontSize = 16.sp,
            color = Color.DarkGray,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Watch Now")
            }

            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Share")
            }
        }
    }
}
