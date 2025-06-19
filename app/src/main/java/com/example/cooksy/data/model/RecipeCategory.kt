package com.example.cooksy.data.model


sealed class RecipeCategory(val name: String, val displayName: String) {
    object BREAKFAST : RecipeCategory("breakfast", "Desayuno")
    object LUNCH : RecipeCategory("lunch", "Almuerzo")
    object DINNER : RecipeCategory("dinner", "Cena")
    object SNACK : RecipeCategory("snack", "Snack")
    object DESSERT : RecipeCategory("dessert", "Postre")
    object VEGETARIAN : RecipeCategory("vegetarian", "Vegetariana")
    object GLUTEN_FREE : RecipeCategory("gluten free", "Sin Gluten")

    companion object {
        val allCategories = listOf(
            BREAKFAST, LUNCH, DINNER, SNACK, DESSERT, VEGETARIAN, GLUTEN_FREE
        )

        fun fromName(name: String?): RecipeCategory? {
            return allCategories.find { it.name.equals(name, ignoreCase = true) }
        }
    }
}

