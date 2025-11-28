package com.example.movie_discovery.Screens

import android.content.res.Configuration
import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movie_discovery.data.AuthState
import com.example.movie_discovery.data.AuthViewModel
import com.example.movie_discovery.ui.theme.*

@Composable
fun NeonText(
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
fun SignInScreen(
    navController: NavController? = null,
    authViewModel: AuthViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val systemLanguage = configuration.locales[0].language

    val isArabic = systemLanguage.startsWith("ar")

    val backgroundBrush = if (isSystemInDarkTheme()) {
        Brush.verticalGradient(
            colors = listOf(DarkNavy, MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), DarkNavy)
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color.White, Color(0xFFF5F5F5), Color.White)
        )
    }
    LaunchedEffect(Unit) {
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            navController?.navigate("home") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
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
                containerColor = (if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else Color(0xFFF5F5F5)) as Color
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isArabic) "تسجيل الدخول" else "Sign In",
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; isEmailError = false },
                    label = {
                        Text(
                            if (isArabic) "البريد الإلكتروني" else "Email",
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else Color.Black.copy(alpha = 0.7f)
                        )
                    },
                    isError = isEmailError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentRed,
                        unfocusedBorderColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else Color.Black.copy(alpha = 0.3f),
                        focusedTextColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else Color.Black
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; isPasswordError = false },
                    label = {
                        Text(
                            if (isArabic) "كلمة المرور" else "Password",
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else Color.Black.copy(alpha = 0.7f)
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = if (passwordVisible)
                                    (if (isArabic) "إخفاء كلمة المرور" else "Hide password")
                                else
                                    (if (isArabic) "إظهار كلمة المرور" else "Show password"),
                                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else Color.Gray
                            )
                        }
                    },
                    isError = isPasswordError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else Color.Black.copy(alpha = 0.3f),
                        focusedTextColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else Color.Black
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Button(
                    onClick = {
                        isEmailError = email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        isPasswordError = password.isBlank()

                        if (isEmailError || isPasswordError) {
                            Toast.makeText(
                                context,
                                if (isArabic) "الرجاء إدخال بريد إلكتروني وكلمة مرور صحيحين"
                                else "Please enter a valid email and password",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            authViewModel.signIn(email, password)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(top = 16.dp, bottom = 8.dp),
                    enabled = authState != AuthState.Loading
                ) {
                    if (authState == AuthState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            if (isArabic) "تسجيل الدخول" else "Sign In",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (isArabic) "مستخدم جديد؟ " else "New user? ",
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else Color.Black.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            Text(
                text = if (isArabic) "إنشاء حساب" else "SIGN UP",
                color = AccentRed,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navController?.navigate("signup") }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                navController?.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
                authViewModel.resetAuthState()
            }

            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                authViewModel.resetAuthState()
            }

            else -> Unit
        }
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
