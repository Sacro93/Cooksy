package com.example.cooksy.presentation.screens.login


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cooksy.R
import com.example.cooksy.presentation.navigation.Routes
import com.example.cooksy.viewModel.session.SessionViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    viewModel: SessionViewModel = viewModel(),
    onSignupClick: () -> Unit = {}
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var keepSession by remember { mutableStateOf(false) } // checkbox para mantener sesión

    val loginState by viewModel.loginState.collectAsState()
    val resetPasswordState by viewModel.resetPasswordState.collectAsState()

    // Manejo del estado de login
    LaunchedEffect(loginState) {
        loginState?.let {
            if (it.isSuccess) {
                if (keepSession) {
                    viewModel.setSessionPersistence(true)
                }
                Toast.makeText(context, "Sesión iniciada", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
                viewModel.clearLoginState()
            } else {
                Toast.makeText(
                    context,
                    it.exceptionOrNull()?.message ?: "Error al iniciar sesión",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearLoginState()
            }
        }
    }

    // Manejo del estado de recuperación
    LaunchedEffect(resetPasswordState) {
        resetPasswordState?.let {
            if (it.isSuccess) {
                Toast.makeText(context, "Correo de recuperación enviado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    context,
                    it.exceptionOrNull()?.message ?: "Error al enviar el correo",
                    Toast.LENGTH_LONG
                ).show()
            }
            viewModel.clearResetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        /*
        // Parte superior con imagen de fondo y logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp))
                .background(Color(0xFF9C27B0)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.elementos),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize().alpha(0.3f)
            )
            Image(
                painter = painterResource(id = R.drawable.cooksy_512px),
                contentDescription = "Logo Cooksy",
                modifier = Modifier.size(150.dp).clip(RoundedCornerShape(32.dp))
            )
        }
*/
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp))
                .background(Color(0xFF9C27B0)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.elementos),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.2f)
            )

            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cooksy_512px),
                    contentDescription = "Logo Cooksy",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Formulario de login
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("Sign in to continue.", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            var showPassword by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val icon = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    val description = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = description
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(checked = keepSession, onCheckedChange = { keepSession = it })
                Text("Mantener sesión iniciada", modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        viewModel.login(email, password)
                    } else {
                        Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Log In", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {
                if (email.isNotBlank()) {
                    viewModel.sendPasswordReset(email)
                } else {
                    Toast.makeText(context, "Escribe tu email primero", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Forgot Password?", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onSignupClick) {
                Text("Signup!", color = Color(0xFF9C27B0))
            }
        }
    }
}
