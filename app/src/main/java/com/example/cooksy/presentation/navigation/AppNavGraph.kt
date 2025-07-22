package com.example.cooksy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cooksy.presentation.StartUpScreen
import com.example.cooksy.presentation.screens.favourites.FavouriteScreen
import com.example.cooksy.presentation.screens.home.HomeScreen
import com.example.cooksy.presentation.screens.ia.CookLabScreen
import com.example.cooksy.presentation.screens.login.LoginScreen
import com.example.cooksy.presentation.screens.place.AddPlaceScreen
import com.example.cooksy.presentation.screens.place.PlaceScreen
import com.example.cooksy.presentation.screens.recipes.CategorySelectionScreen
import com.example.cooksy.presentation.screens.recipes.RecipeListScreen
import com.example.cooksy.presentation.screens.register.RegisterScreen
import com.example.cooksy.presentation.screens.supermarket.SupermarketListScreen
import com.example.cooksy.presentation.screens.virals.ViralRecipesScreen
import com.example.cooksy.viewModel.recipe.RecipeViewModel
import com.example.cooksy.presentation.screens.recipes.RecipeDetailScreen
import com.example.cooksy.presentation.screens.recipes.RecipeSearchScreen
import com.example.cooksy.presentation.screens.virals.AddViralRecipeScreen
import com.example.cooksy.viewModel.session.SessionViewModel
import com.example.cooksy.viewModel.place.PlaceViewModel
import com.example.cooksy.viewModel.viral.ViralRecipeViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    recipeViewModel: RecipeViewModel,
    viralRecipeViewModel: ViralRecipeViewModel,
    placeViewModel: PlaceViewModel,


) {
    NavHost(
        navController = navController,
        startDestination = Routes.STARTUP
    ) {

        composable(Routes.STARTUP) {
            StartUpScreen(navController = navController, sessionViewModel = sessionViewModel)
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                onSignupClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(navController = navController, viewModel = sessionViewModel)
        }


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

        composable(Routes.RECIPE_SEARCH) {
            RecipeSearchScreen(navController = navController, viewModel = recipeViewModel)
        }


        composable(Routes.VIRAL_RECIPES) {
            ViralRecipesScreen(
                navController = navController,
                viewModel = viralRecipeViewModel
            )
        }

        composable(Routes.ADD_VIRAL_RECIPE) {
            AddViralRecipeScreen(
                navController = navController,
                viewModel = viralRecipeViewModel
            )
        }

        composable(Routes.PLACE_LIST) {
            PlaceScreen(
                navController = navController,
                viewModel = placeViewModel
            )
        }

        composable(Routes.ADD_PLACE) {
            AddPlaceScreen(
                navController = navController,
                viewModel = placeViewModel
            )
        }

        composable(Routes.SUPERMARKET_LIST) { SupermarketListScreen() }
        composable(Routes.FAVOURITES) { FavouriteScreen() }
        composable(Routes.COOK_LAB) { CookLabScreen() }
    }
}
