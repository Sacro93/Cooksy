package com.example.cooksy.presentation.screens.profile

/*
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.cooksy.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cooksy.viewModel.SessionViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.navigation.NavHostController
import com.example.cooksy.presentation.navigation.Routes

@Composable
fun ProfileScreen(sessionViewModel: SessionViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf("") }
    var avatarKey by remember { mutableStateOf("chefcita") }
    var email by remember { mutableStateOf("") }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(true) }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        sessionViewModel.clearUserProfile()
        sessionViewModel.getUserProfile()
    }

    val userProfileState by sessionViewModel.userProfile.collectAsState()

    LaunchedEffect(userProfileState) {
        userProfileState?.let { result ->
            result.fold(
                onSuccess = { user ->
                    fullName = user.fullName
                    avatarKey = user.avatar
                    email = user.email
                    isLoading = false
                },
                onFailure = {
                    Toast.makeText(context, "Error al cargar perfil", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            )
        }
    }

    val avatarRes = when (avatarKey) {
        "chefcito" -> R.drawable.chefcito
        "chefcita" -> R.drawable.chefcita
        else -> R.drawable.cooksy_512px
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF512DA8),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable {
                    avatarKey = if (avatarKey == "chefcita") "chefcito" else "chefcita"
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = {},
            enabled = false,
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Contraseña actual") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Nueva contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    val profileResult = sessionViewModel.performProfileUpdate(fullName, avatarKey)
                    val passwordResult = if (currentPassword.isNotBlank() && newPassword.isNotBlank()) {
                        sessionViewModel.performPasswordChange(currentPassword, newPassword)
                    } else {
                        Result.success(Unit)
                    }

                    if (profileResult.isSuccess && passwordResult.isSuccess) {
                        showSuccess = true
                        currentPassword = ""
                        newPassword = ""
                    } else {
                        Toast.makeText(context, "Error al guardar cambios", Toast.LENGTH_SHORT).show()
                    }

                    sessionViewModel.clearUpdateProfileState()
                    sessionViewModel.clearChangePasswordState()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF512DA8))
        ) {
            Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar cambios", color = Color.White)
        }

        if (showSuccess) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Perfil actualizado correctamente!",
                color = Color(0xFF4CAF50),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = {
                sessionViewModel.logout()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.HOME) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión", color = Color.Red)
        }
    }
}
*/