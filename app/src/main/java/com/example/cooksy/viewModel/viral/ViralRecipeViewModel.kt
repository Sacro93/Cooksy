package com.example.cooksy.viewModel.viral

import androidx.lifecycle.ViewModel
import com.example.cooksy.data.model.viralRecipes.ViralRecipe
import com.example.cooksy.data.repository.ViralRecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ViralRecipeViewModel(private val repository: ViralRecipeRepository) : ViewModel() {
    private val _recipes = MutableStateFlow<List<ViralRecipe>>(emptyList())
    val recipes: StateFlow<List<ViralRecipe>> = _recipes.asStateFlow()

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        _recipes.value = repository.getAll()
    }

    fun addRecipe(recipe: ViralRecipe) {
        repository.add(recipe)
        loadRecipes()
    }

    fun deleteRecipe(recipe: ViralRecipe) {
        repository.delete(recipe)
        loadRecipes()
    }
}

