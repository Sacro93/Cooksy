package com.example.cooksy.data.model.recipes


sealed class RecipeCategory(
    val name: String,
    val displayName: String,
    val imageResName: String // nombre del recurso en drawable, sin extensión
) {
    object BREAKFAST : RecipeCategory("breakfast", "Desayuno", "breakfast_time")
    object LUNCH : RecipeCategory("lunch", "Almuerzo", "lunch_time")
    object DINNER : RecipeCategory("dinner", "Cena", "dinner_time")
    object SNACK : RecipeCategory("snack", "Snack", "snack_time_2")
    object DESSERT : RecipeCategory("dessert", "Postre", "dessert")
    object VEGETARIAN : RecipeCategory("vegetarian", "Vegetariana", "vegetarian")
    object GLUTEN_FREE : RecipeCategory("gluten free", "Sin Gluten", "gluten_free")

    companion object {
        val allCategories = listOf(
            BREAKFAST, LUNCH, DINNER, SNACK, DESSERT, VEGETARIAN, GLUTEN_FREE
        )

        fun fromName(name: String?): RecipeCategory? {
            return allCategories.find { it.name.equals(name, ignoreCase = true) }
        }
    }
}


