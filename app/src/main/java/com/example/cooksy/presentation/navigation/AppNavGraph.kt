package com.example.cooksy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cooksy.presentation.screens.favourites.FavouriteScreen
import com.example.cooksy.presentation.screens.home.HomeScreen
import com.example.cooksy.presentation.screens.ia.CookLabScreen
import com.example.cooksy.presentation.screens.login.LoginScreen
import com.example.cooksy.presentation.screens.profile.ProfileScreen
import com.example.cooksy.presentation.screens.recipes.RecipeDetailScreen
import com.example.cooksy.presentation.screens.recipes.RecipeListScreen
import com.example.cooksy.presentation.screens.register.RegisterScreen
import com.example.cooksy.presentation.screens.supermarket.SupermarketListScreen
import com.example.cooksy.presentation.screens.virals.ViralRecipesScreen
import com.example.cooksy.viewModel.RecipeViewModel
import com.example.cooksy.data.model.Recipe
import androidx.navigation.NavType

@Composable
fun AppNavGraph(navController: NavHostController) {

    val recipeViewModel: RecipeViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.LOGIN) {
            LoginScreen()
        }
        composable(Routes.REGISTER) {
            RegisterScreen()
        }
        composable(Routes.HOME) {
            HomeScreen()
        }
        // List Screen
        composable(Routes.RECIPE_LIST) {
            RecipeListScreen(navController = navController, viewModel = recipeViewModel)
        }
        composable(
            route = Routes.RECIPE_DETAIL,
            arguments = listOf(
                navArgument("recipeId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1

            val recipesState = recipeViewModel.recipes.collectAsState()

            val recipe = recipesState.value.find { it.id == recipeId }

            recipe?.let { recipeSelected: Recipe ->
                RecipeDetailScreen(recipe = recipeSelected, navController = navController)
            }
        }


        composable(Routes.VIRAL_RECIPES) {
            ViralRecipesScreen()
        }
        composable(Routes.SUPERMARKET_LIST) {
            SupermarketListScreen()
        }
        composable(Routes.FAVOURITES) {
            FavouriteScreen()
        }
        composable(Routes.PROFILE) {
            ProfileScreen()
        }
        composable(Routes.COOK_LAB) {
            CookLabScreen()
        }
    }
}
