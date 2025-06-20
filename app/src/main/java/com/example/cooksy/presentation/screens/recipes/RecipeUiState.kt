package com.example.cooksy.presentation.screens.recipes

import com.example.cooksy.data.model.recipes.Recipe

sealed class RecipeUiState {
    object Loading : RecipeUiState()
    data class Success(val recipes: List<Recipe>) : RecipeUiState()
    data class Error(val message: String) : RecipeUiState()
}
