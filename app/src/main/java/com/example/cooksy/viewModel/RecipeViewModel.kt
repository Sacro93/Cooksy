package com.example.cooksy.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cooksy.data.repository.RecipeRepository
import androidx.lifecycle.viewModelScope
import com.example.cooksy.presentation.screens.recipes.RecipeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*✔ Lógica reactiva simple.

✔ La UI nunca queda en estado inválido.

✔ Evitamos llamadas duplicadas si ya teníamos los datos cargados.

✔ El ViewModel toma control de cuándo se llama por primera vez, y no la UI.*/

class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Loading)
    val uiState: StateFlow<RecipeUiState> = _uiState

    init {
        fetchRecipes()
    }

    fun fetchRecipes(number: Int = 10, tags: String = "") {
        viewModelScope.launch {
            try {
                val response = repository.getRandomRecipes(number, tags)
                Log.d("API_RESPONSE", "Fetched: ${response.recipes.size} recipes")

                response.recipes.forEach { recipe ->
                    Log.d("API_RECIPE", "Recipe: ${recipe.title}, Ingredients: ${recipe.ingredients}")
                }

                _uiState.value = RecipeUiState.Success(response.recipes)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("API_ERROR", "Error fetching recipes", e)
                _uiState.value = RecipeUiState.Error("Failed to load recipes")
            }
        }
    }

}
