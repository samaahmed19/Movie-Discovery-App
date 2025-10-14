package com.example.movie_discovery.Screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.movie_discovery.ui.theme.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.input.VisualTransformation
import com.example.movie_discovery.data.AuthState
import com.example.movie_discovery.data.AuthViewModel

@Composable
fun Neontext(
    text: String,
    neonColor: Color,
    modifier: Modifier = Modifier,
    fontStyle: FontStyle = FontStyle.Normal
) {
    val baseStyle = LocalTextStyle.current.copy(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = fontStyle
    )

    Box(modifier = modifier) {
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
        Text(
            text = text,
            color = Color.White,
            style = baseStyle,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController? = null) {

    val scrollState = rememberScrollState()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val backgroundBrush = if (isSystemInDarkTheme()) {
        Brush.verticalGradient(
            colors = listOf(DarkNavy, CardBackground.copy(alpha = 0.8f), DarkNavy)
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color.White, Color(0xFFF5F5F5), Color.White)
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
                neonColor = Color.Cyan.copy(alpha = 0.9f),
                fontStyle = FontStyle.Italic
            )
            NeonText(
                text = "Discovery",
                neonColor = AccentRed.copy(alpha = 0.8f),
                fontStyle = FontStyle.Italic
            )
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSystemInDarkTheme()) CardBackground else Color(0xFFF5F5F5)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sign Up",
                    color = if (isSystemInDarkTheme()) TextPrimary else Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentRed,
                            unfocusedBorderColor = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.3f),
                            focusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black,
                            unfocusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentRed,
                            unfocusedBorderColor = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.3f),
                            focusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black,
                            unfocusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username", color = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.7f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentRed,
                        unfocusedBorderColor = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.3f),
                        focusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.7f)) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = if (isSystemInDarkTheme()) TextSecondary else Color.Gray
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentRed,
                        unfocusedBorderColor = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.3f),
                        focusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password", color = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.7f)) },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                                tint = if (isSystemInDarkTheme()) TextSecondary else Color.Gray
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentRed,
                        unfocusedBorderColor = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.3f),
                        focusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                Button(
                    onClick = {
                        navController?.navigate("home") {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(70.dp).padding(top = 16.dp, bottom = 8.dp)
                ) {
                    Text(
                        "Sign Up",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Already have an account? ",
                color = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            Text(
                text = "SIGN IN",
                color = AccentRed,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navController?.navigate("signIn") }
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewSignUpScreenLight() {
    MoviesTheme(darkTheme = false) {
        SignUpScreen(navController = null)
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignUpScreenDark() {
    MoviesTheme(darkTheme = true) {
        SignUpScreen(navController = null)
    }
}