package com.example.cooksy.viewModel.recipe

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cooksy.data.repository.RecipeRepository
import com.example.cooksy.data.model.recipes.RecipeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Loading)
    val uiState: StateFlow<RecipeUiState> = _uiState

    fun loadRecipesByCategory(type: String) {
        viewModelScope.launch {
            Log.d("RecipeViewModel", "Solicitando recetas con tipo: $type")
            _uiState.value = RecipeUiState.Loading
            try {
                val list = repository.getRecipes(type = type)
                _uiState.value = RecipeUiState.Success(list)
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error: ${e.message}", e)
                _uiState.value = RecipeUiState.Error("Error al cargar recetas.")
            }
        }
    }

    fun loadRecipeDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = RecipeUiState.Loading
            try {
                val recipe = repository.getRecipeById(id)
                _uiState.value = RecipeUiState.Success(listOf(recipe))
            } catch (e: Exception) {
                _uiState.value = RecipeUiState.Error("No se pudo cargar el detalle.")
            }
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _uiState.value = RecipeUiState.Loading
            try {
                val results = repository.searchRecipes(query)
                _uiState.value = RecipeUiState.Success(results)
            } catch (e: Exception) {
                _uiState.value = RecipeUiState.Error("Error al buscar recetas.")
            }
        }
    }


}