package com.example.cooksy.viewModel

import androidx.lifecycle.ViewModel
import com.example.cooksy.data.repository.RecipeRepository
import androidx.lifecycle.viewModelScope
import com.example.cooksy.presentation.screens.recipes.RecipeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Loading)
    val uiState: StateFlow<RecipeUiState> = _uiState

    fun fetchRecipes(number: Int = 10, tags: String = "") {
        viewModelScope.launch {
            try {
                val response = repository.getRandomRecipes(number, tags)
                _uiState.value = RecipeUiState.Success(response.recipes)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = RecipeUiState.Error("Failed to load recipes")
            }
        }
    }
}
