package com.example.cooksy.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.cooksy.viewModel.session.DEFAULT_AVATAR
import com.example.cooksy.viewModel.session.SessionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel = viewModel() // Use existing instance or get from factory
) {
    val userProfile by sessionViewModel.userProfile.collectAsState()
    val localAvatar by sessionViewModel.localAvatarPreference.collectAsState()
    val isLoading by sessionViewModel.profileLoading.collectAsState()
    val updateProfileResult by sessionViewModel.updateProfileResult.collectAsState()
    val changePasswordResult by sessionViewModel.changePasswordResult.collectAsState()

    var newName by remember(userProfile?.fullName) { mutableStateOf(userProfile?.fullName ?: "") }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmNewPasswordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(userProfile) {
        if (userProfile == null && sessionViewModel.isUserLoggedIn) {
            sessionViewModel.loadUserProfile()
        }
        newName = userProfile?.fullName ?: ""
    }

    LaunchedEffect(updateProfileResult) {
        updateProfileResult?.let { result ->
            if (result.isSuccess) {
                scope.launch { snackbarHostState.showSnackbar("Nombre actualizado correctamente.") }
            } else {
                scope.launch { snackbarHostState.showSnackbar("Error al actualizar nombre: ${result.exceptionOrNull()?.message}") }
            }
            sessionViewModel.clearUpdateProfileResult()
        }
    }

    LaunchedEffect(changePasswordResult) {
        changePasswordResult?.let { result ->
            if (result.isSuccess) {
                scope.launch { snackbarHostState.showSnackbar("Contraseña cambiada correctamente.") }
                currentPassword = ""
                newPassword = ""
                confirmNewPassword = ""
            } else {
                scope.launch { snackbarHostState.showSnackbar("Error al cambiar contraseña: ${result.exceptionOrNull()?.message}") }
            }
            sessionViewModel.clearChangePasswordResult()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF9C27B0).copy(alpha = 0.3f),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF9C27B0), Color(0xFF1565C0))
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading && userProfile == null) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    // Avatar Selection
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Elige tu avatar:", color = Color.White, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AvatarImage(
                            avatarName = "chefcito",
                            isSelected = localAvatar == "chefcito",
                            onClick = { sessionViewModel.updateLocalAvatarPreference("chefcito") }
                        )
                        AvatarImage(
                            avatarName = "chefcita",
                            isSelected = localAvatar == "chefcita",
                            onClick = { sessionViewModel.updateLocalAvatarPreference("chefcita") }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // Email (Read-only)
                    Text(
                        text = "Email: ${userProfile?.email ?: "Cargando..."}",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Change Name
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nombre Completo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.White.copy(alpha = 0.1f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                            disabledContainerColor = Color.White.copy(alpha = 0.1f),
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (newName.isNotBlank() && newName != userProfile?.fullName) {
                                sessionViewModel.updateUserFullName(newName)
                            }
                        },
                        enabled = !isLoading && newName.isNotBlank() && newName != userProfile?.fullName,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                    ) {
                        Text("Actualizar Nombre")
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // Change Password
                    Text("Cambiar Contraseña", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = "Contraseña Actual",
                        isVisible = currentPasswordVisible,
                        onVisibilityChange = { currentPasswordVisible = !currentPasswordVisible }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = "Nueva Contraseña",
                        isVisible = newPasswordVisible,
                        onVisibilityChange = { newPasswordVisible = !newPasswordVisible }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordTextField(
                        value = confirmNewPassword,
                        onValueChange = { confirmNewPassword = it },
                        label = "Confirmar Nueva Contraseña",
                        isVisible = confirmNewPasswordVisible,
                        onVisibilityChange = { confirmNewPasswordVisible = !confirmNewPasswordVisible }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (newPassword == confirmNewPassword && newPassword.length >= 6) {
                                sessionViewModel.changeUserPassword(currentPassword, newPassword)
                            } else if (newPassword != confirmNewPassword) {
                                scope.launch { snackbarHostState.showSnackbar("Las nuevas contraseñas no coinciden.") }
                            } else {
                                scope.launch { snackbarHostState.showSnackbar("La nueva contraseña debe tener al menos 6 caracteres.") }
                            }
                        },
                        enabled = !isLoading && currentPassword.isNotBlank() && newPassword.isNotBlank() && confirmNewPassword.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                    ) {
                        Text("Cambiar Contraseña")
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    // Logout Button
                    Button(
                        onClick = {
                            sessionViewModel.logout()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.HOME) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
             if (isLoading && userProfile != null) { // Show loader overlay if already loaded but doing an operation
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

@Composable
fun AvatarImage(avatarName: String, isSelected: Boolean, onClick: () -> Unit) {
    val imageRes = if (avatarName == "chefcito") R.drawable.chefcito else R.drawable.chefcita
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color.White.copy(alpha = 0.5f) else Color.Transparent)
            .border(
                width = 3.dp,
                color = if (isSelected) Color(0xFFE91E63) else Color.White.copy(alpha = 0.7f),
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .padding(8.dp), // Padding inside the border, before the image
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = avatarName,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    onVisibilityChange: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (isVisible) "Ocultar contraseña" else "Mostrar contraseña"
            IconButton(onClick = onVisibilityChange) {
                Icon(imageVector = image, description)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.White.copy(alpha = 0.1f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
            disabledContainerColor = Color.White.copy(alpha = 0.1f),
            cursorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
            focusedTrailingIconColor = Color.White.copy(alpha = 0.7f),
            unfocusedTrailingIconColor = Color.White.copy(alpha = 0.7f)
        )
    )
}
