package com.example.cooksy.data.repository

import com.example.cooksy.data.model.viralRecipes.ViralRecipe
import com.example.cooksy.data.storage.ViralRecipeStorage

class ViralRecipeRepository(private val storage: ViralRecipeStorage) {
    fun getAll(): List<ViralRecipe> = storage.loadRecipes()

    fun add(recipe: ViralRecipe) {
        val current = storage.loadRecipes().toMutableList()
        current.add(recipe)
        storage.saveRecipes(current)
    }

    fun delete(recipe: ViralRecipe) {
        val current = storage.loadRecipes().filterNot { it.id == recipe.id }
        storage.saveRecipes(current)
    }
}
