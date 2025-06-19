package com.example.cooksy.presentation.screens.recipes


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cooksy.viewModel.RecipeViewModel
import androidx.compose.runtime.getValue

@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    viewModel: RecipeViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is RecipeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is RecipeUiState.Success -> {
            val recipes = (uiState as RecipeUiState.Success).recipes
            val recipe = recipes.find { it.id == recipeId }

            recipe?.let {
                RecipeDetailContent(recipe)
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Recipe not found", color = Color.Red)
                }
            }
        }
        is RecipeUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text((uiState as RecipeUiState.Error).message, color = Color.Red)
            }
        }
    }
}
