package com.example.cooksy.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cooksy.presentation.screens.favourites.FavouriteScreen
import com.example.cooksy.presentation.screens.home.HomeScreen
import com.example.cooksy.presentation.screens.ia.CookLabScreen
import com.example.cooksy.presentation.screens.login.LoginScreen
import com.example.cooksy.presentation.screens.profile.ProfileScreen
import com.example.cooksy.presentation.screens.recipes.CategorySelectionScreen
import com.example.cooksy.presentation.screens.recipes.RecipeListScreen
import com.example.cooksy.presentation.screens.register.RegisterScreen
import com.example.cooksy.presentation.screens.supermarket.SupermarketListScreen
import com.example.cooksy.presentation.screens.virals.ViralRecipesScreen
import com.example.cooksy.viewModel.RecipeViewModel
import com.example.cooksy.presentation.screens.recipes.RecipeDetailScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    recipeViewModel: RecipeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.LOGIN) { LoginScreen() }
        composable(Routes.REGISTER) { RegisterScreen() }

        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }

        composable(Routes.CATEGORY_SELECTOR) {
            CategorySelectionScreen(navController = navController)
        }

        composable(
            route = Routes.RECIPE_LIST_BY_CATEGORY,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName")
            RecipeListScreen(
                navController = navController,
                categoryName = categoryName,
                viewModel = recipeViewModel
            )
        }

        composable(
            route = Routes.RECIPE_DETAIL,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1
            RecipeDetailScreen(
                recipeId = recipeId,
                viewModel = recipeViewModel,
                navController = navController
            )
        }

        // Otras pantallas
        composable(Routes.VIRAL_RECIPES) { ViralRecipesScreen() }
        composable(Routes.SUPERMARKET_LIST) { SupermarketListScreen() }
        composable(Routes.FAVOURITES) { FavouriteScreen() }
        composable(Routes.PROFILE) { ProfileScreen() }
        composable(Routes.COOK_LAB) { CookLabScreen() }
    }
}
