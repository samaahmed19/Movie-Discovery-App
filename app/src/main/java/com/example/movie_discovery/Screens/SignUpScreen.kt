package com.example.movie_discovery.Screens

import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isFirstNameError by remember { mutableStateOf(false) }
    var isLastNameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    // 👇 تحديد لغة النظام (عربي ولا إنجليزي)
    val systemLanguage = context.resources.configuration.locales[0].language
    val isArabic = systemLanguage.startsWith("ar")

    val backgroundBrush = if (isSystemInDarkTheme()) {
        Brush.verticalGradient(
            colors = listOf(DarkNavy, CardBackground.copy(alpha = 0.8f), DarkNavy)
        )
    } else {
        Brush.verticalGradient(colors = listOf(Color.White, Color(0xFFF5F5F5), Color.White))
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
                    text = if (isArabic) "إنشاء حساب" else "Sign Up",
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
                        onValueChange = { firstName = it; isFirstNameError = false },
                        label = {
                            Text(
                                if (isArabic) "الاسم الأول" else "First Name",
                                color = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.7f)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        isError = isFirstNameError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentRed,
                            unfocusedBorderColor = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.3f),
                            focusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black,
                            unfocusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black
                        )
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it; isLastNameError = false },
                        label = {
                            Text(
                                if (isArabic) "اسم العائلة" else "Last Name",
                                color = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.7f)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        isError = isLastNameError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentRed,
                            unfocusedBorderColor = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.3f),
                            focusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black,
                            unfocusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black
                        )
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; isEmailError = false },
                    label = {
                        Text(
                            if (isArabic) "البريد الإلكتروني" else "Email",
                            color = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.7f)
                        )
                    },
                    isError = isEmailError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentRed,
                        unfocusedBorderColor = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.3f),
                        focusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) TextPrimary else Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            if (isArabic) "كلمة المرور" else "Password",
                            color = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.7f)
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = if (passwordVisible)
                                    (if (isArabic) "إخفاء كلمة المرور" else "Hide password")
                                else
                                    (if (isArabic) "إظهار كلمة المرور" else "Show password"),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = {
                        Text(
                            if (isArabic) "تأكيد كلمة المرور" else "Confirm Password",
                            color = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.7f)
                        )
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = image,
                                contentDescription = if (confirmPasswordVisible)
                                    (if (isArabic) "إخفاء كلمة المرور" else "Hide password")
                                else
                                    (if (isArabic) "إظهار كلمة المرور" else "Show password"),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Button(
                    onClick = {
                        isFirstNameError = firstName.isBlank()
                        isLastNameError = lastName.isBlank()
                        isEmailError =
                            email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        isPasswordError = password.isBlank()

                        val hasError =
                            isFirstNameError || isLastNameError || isEmailError || isPasswordError

                        if (hasError) {
                            Toast.makeText(
                                context,
                                if (isArabic) "يرجى ملء جميع الحقول بشكل صحيح" else "Please fill all fields correctly",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (password != confirmPassword) {
                            Toast.makeText(
                                context,
                                if (isArabic) "كلمتا المرور غير متطابقتين" else "Passwords do not match",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            authViewModel.signUp(firstName, lastName, email, password)
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
                            if (isArabic) "إنشاء حساب" else "Sign Up",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(
                if (isArabic) "لديك حساب بالفعل؟ " else "Already have an account? ",
                color = if (isSystemInDarkTheme()) TextSecondary else Color.Black.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            Text(
                text = if (isArabic) "تسجيل الدخول" else "SIGN IN",
                color = AccentRed,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navController.navigate("signIn") }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                navController.navigate("signIn") {
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
