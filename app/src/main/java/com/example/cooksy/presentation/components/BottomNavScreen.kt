package com.example.cooksy.presentation.components
import com.example.cooksy.R

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home

sealed class BottomNavScreen(
    val route: String,
    val icon: Any, // Puede ser ImageVector o Int
    val label: String
) {
    object Home : BottomNavScreen("home", R.drawable.casa_linea, "Home")
    object CookLab : BottomNavScreen("cooklab", R.drawable.cooklab_negro, "CookLab")
    object Favorites : BottomNavScreen("favorites", R.drawable.corazon_blanco, "Favorites")
}
