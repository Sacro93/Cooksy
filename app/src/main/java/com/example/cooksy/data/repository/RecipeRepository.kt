package com.example.cooksy.data.repository

import android.util.Log
import com.example.cooksy.data.model.recipes.Recipe
import com.example.cooksy.data.remote.RecipeService

//Offset para scroll infinito = desactivado

class RecipeRepository(
    private val recipeService: RecipeService
) {

    //listar muchas recetas
    suspend fun getRecipes(type: String? = null/*, offset: Int = 0 */): List<Recipe> {
        val response = recipeService.getRecipes(type = type,addInfo = true)
        Log.d("RecipeRepository", "Tipo: $type - Recetas obtenidas: ${response.results.size}")

        return response.results
    }

    suspend fun getRecipeById(id: Int): Recipe {
        return recipeService.getRecipeDetail(id)
    }


}

