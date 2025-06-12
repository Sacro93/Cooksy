package com.example.cooksy.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Science
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavScreen(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavScreen("home", Icons.Filled.Home, "Home")
    object CookLab : BottomNavScreen("cooklab", Icons.Filled.Science, "CookLab")
    object Favorites : BottomNavScreen("favorites", Icons.Filled.FavoriteBorder, "Favorites")
}
