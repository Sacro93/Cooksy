package com.example.cooksy.presentation.screens.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cooksy.viewModel.RecipeViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.cooksy.data.model.RecipeCategory
import com.example.cooksy.presentation.navigation.Routes

@Composable
fun RecipeListScreen(
    navController: NavHostController,
    categoryName: String?,
    viewModel: RecipeViewModel
) {
    val category = RecipeCategory.allCategories.find { it.name == categoryName }

    LaunchedEffect(category) {
        category?.let { viewModel.loadRecipesByCategory(it.name) }
    }

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        category?.let { selectedCategory ->
            Text(
                text = "Recetas de ${selectedCategory.name.uppercase()}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        when (uiState) {
            is RecipeUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is RecipeUiState.Success -> {
                val recipes = (uiState as RecipeUiState.Success).recipes

                if (recipes.isEmpty()) {
                    Text("No se encontraron recetas")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(recipes) { recipe ->
                            RecipeItem(recipe = recipe) {
                                navController.navigate(Routes.recipeDetail(recipe.id))
                            }
                        }
                    }
                }
            }

            is RecipeUiState.Error -> {
                Text(
                    (uiState as RecipeUiState.Error).message,
                    color = Color.Red
                )
            }

            else -> Text("Selecciona una categoría")
        }
    }
}
