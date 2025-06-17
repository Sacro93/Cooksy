package com.example.cooksy.data.repository

import com.example.cooksy.data.remote.ApiConfig
import com.example.cooksy.data.remote.RecipeService

class RecipeRepository(
    private val recipeService: RecipeService
) {
    suspend fun getRandomRecipes(number: Int = 10, tags: String = "") =
        recipeService.getRandomRecipes(number, tags, ApiConfig.API_KEY)
}
