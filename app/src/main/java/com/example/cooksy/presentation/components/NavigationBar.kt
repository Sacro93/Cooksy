package com.example.cooksy.presentation.components


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.CookLab,
        BottomNavScreen.Favorites
    )

    NavigationBar(
        containerColor = Color.Transparent // Sin color de fondo
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (val icon = screen.icon) {
                        is ImageVector -> Icon(
                            imageVector = icon,
                            contentDescription = screen.label
                        )
                        is Int -> Icon(
                            painter = painterResource(id = icon),
                            contentDescription = screen.label
                        )
                        else -> throw IllegalArgumentException("Icon type not supported")
                    }
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(navController = navController)
}