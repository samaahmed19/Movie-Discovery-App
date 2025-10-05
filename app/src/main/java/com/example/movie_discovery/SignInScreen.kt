package com.example.movie_discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import compose.icons.SimpleIcons
import compose.icons.simpleicons.Google
import com.example.movie_discovery.ui.theme.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun NeonText (
text: String,
neonColor: Color,
modifier: Modifier = Modifier
){
Box(modifier = Modifier){
Text(
    text = text,
fontSize =32.sp,
    fontWeight = FontWeight.Bold,
    color = Color.Transparent,
    style = LocalTextStyle.current.copy(
        shadow = Shadow(
            color = neonColor.copy(alpha = 0.5f),
            offset = androidx.compose.ui.geometry.Offset(0f, 0f),
            blurRadius = 15f
        )
    ),
textAlign = TextAlign.Center
)
Text(
    text =text,
    fontSize =32.sp,
fontWeight = FontWeight.Bold,
    color =Color.White,
textAlign = TextAlign.Center
)

}

}