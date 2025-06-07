package com.example.cooksy.data.repository

import com.example.cooksy.data.model.RecipeResponse
import com.example.cooksy.data.remote.RecipeService
import com.example.cooksy.data.remote.RetrofitInstance
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeService: RecipeService
){

    private val apiKey = "a17ae3189574486989dca7ce80060f3a"

    suspend fun getRandomRecipes(number: Int = 10, tags: String = "") =
        recipeService.getRandomRecipes(number, tags, apiKey)
}
