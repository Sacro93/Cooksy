package com.example.cooksy.viewModel

import androidx.lifecycle.ViewModel
import com.example.cooksy.data.repository.RecipeRepository
import androidx.lifecycle.viewModelScope
import com.example.cooksy.data.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//✅ ViewModel que trae recetas y expone un StateFlow de recetas.

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {


    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    fun fetchRecipes(number: Int = 10, tags: String = "") {
        viewModelScope.launch {
            try {
                val response = repository.getRandomRecipes(number, tags)
                _recipes.value = response.recipes
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}