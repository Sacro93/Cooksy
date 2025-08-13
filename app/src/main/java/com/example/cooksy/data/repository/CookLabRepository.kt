package com.example.cooksy.data.repository

import com.example.cooksy.data.model.recipes.Recipe

/**
 * Interface for the CookLab repository, defining a contract for fetching recipe suggestions.
 */
interface CookLabRepository {


    suspend fun getRecipeSuggestions(userIngredientsText: List<String>): Result<List<Recipe>>
}
