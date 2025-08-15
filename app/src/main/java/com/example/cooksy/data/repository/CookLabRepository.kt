package com.example.cooksy.data.repository

import com.example.cooksy.data.model.recipes.Recipe


interface CookLabRepository {


    suspend fun getRecipeSuggestions(userIngredientsText: List<String>): Result<List<Recipe>>
}
