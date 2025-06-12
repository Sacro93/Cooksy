package com.example.cooksy.data.repository

import com.example.cooksy.data.remote.RecipeService

class RecipeRepository (
    private val recipeService: RecipeService
){

    private val apiKey = "a17ae3189574486989dca7ce80060f3a"

    suspend fun getRandomRecipes(number: Int = 10, tags: String = "") =
        recipeService.getRandomRecipes(number, tags, apiKey)
}
