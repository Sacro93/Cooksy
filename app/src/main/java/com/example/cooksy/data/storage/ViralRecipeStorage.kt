package com.example.cooksy.data.storage

import android.content.Context
import com.example.cooksy.data.model.viralRecipes.ViralRecipe
import kotlinx.serialization.json.Json
import java.io.File
import kotlinx.serialization.encodeToString


class ViralRecipeStorage(private val context: Context) {
    private val fileName = "viral_recipes.json"
    private val json = Json { prettyPrint = true }

    fun saveRecipes(recipes: List<ViralRecipe>) {
        val file = File(context.filesDir, fileName)
        file.writeText(json.encodeToString(recipes))
    }

    fun loadRecipes(): List<ViralRecipe> {
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            try {
                json.decodeFromString(file.readText())
            } catch (e: Exception) {
                emptyList()
            }
        } else emptyList()
    }
}
