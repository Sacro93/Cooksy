package com.example.cooksy.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cooksy.presentation.navigation.Routes
import com.example.cooksy.viewModel.SessionViewModel
import kotlinx.coroutines.delay

@Composable
fun StartUpScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(1000) // Podés convertirlo en constante si querés reutilizarlo

        val isLoggedIn = sessionViewModel.isUserLoggedIn
        val isVerified = sessionViewModel.isEmailVerified()

        val nextRoute = if (isLoggedIn && isVerified) {
            Routes.HOME
        } else {
            Routes.LOGIN
        }

        navController.navigate(nextRoute) {
            popUpTo(Routes.STARTUP) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
