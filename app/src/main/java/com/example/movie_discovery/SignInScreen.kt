package com.example.movie_discovery

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.example.movie_discovery.NeonText
import androidx.compose.ui.tooling.preview.Preview

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
    color = MaterialTheme.colorScheme.onBackground,
textAlign = TextAlign.Center
)

}

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController?) {

    val scrollState = rememberScrollState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val backgroundBrush = if(isSystemInDarkTheme()){
        Brush.verticalGradient(
            colors = listOf(DarkNavy , CardBackground.copy(alpha = 0.8f) , DarkNavy)
        )
    } else{
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFFFFF),
                Color(0xFFF5F5F5),
                Color(0xFFFFFFFF)
                )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(horizontal = 24.dp)
            .verticalScroll(scrollState)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            NeonText(
                text = "Movie",
                neonColor = Color.Cyan.copy(alpha = 0.9f)
            )
            NeonText(
                text = "Discovery",
                neonColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )

        }
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sign In",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(
                        "Username",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(
                        "Password",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(top = 16.dp, bottom = 8.dp)
                ) {
                    Text("Sign In", color = MaterialTheme.colorScheme.onPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick ={
                    navController?.navigate("home"){
                        popUpTo(navController.graph.id){
                            inclusive = true
                        }
                    }
                },
colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onSurface),
shape = CircleShape,
                contentPadding = PaddingValues(12.dp),
modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        imageVector = SimpleIcons.Google,
                        contentDescription = "Google Sign In",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
            }
            }
            }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("New user? ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontSize = 16.sp)
            Text(
                text = "SIGN UP",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navController?.navigate("signup") }
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        }
    }

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewSignInScreenLight() {
    MoviesTheme(darkTheme = false) {
        SignInScreen(navController = null)
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignInScreenDark() {
    MoviesTheme(darkTheme = true) {
        SignInScreen(navController = null)
    }
}