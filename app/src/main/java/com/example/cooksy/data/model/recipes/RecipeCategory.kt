package com.example.cooksy.data.model.recipes


sealed class RecipeCategory(
    val name: String,
    val displayName: String,
    val imageResName: String
) {
    object BREAKFAST : RecipeCategory("breakfast", "Desayuno", "breakfast_time")
    object LUNCH : RecipeCategory("main course", "Almuerzo", "lunch_time")
    object DINNER : RecipeCategory("main course", "Cena", "dinner_time")
    object SNACK : RecipeCategory("snack", "Snack", "snack_time_2")
    object DESSERT : RecipeCategory("dessert", "Postre", "dessert")
    object VEGETARIAN : RecipeCategory("main course", "Vegetariana", "vegetarian")
    object GLUTEN_FREE : RecipeCategory("main course", "Sin Gluten", "gluten_free")

    companion object {
        val allCategories = listOf(
            BREAKFAST, LUNCH, DINNER, SNACK, DESSERT, VEGETARIAN, GLUTEN_FREE,
        )

        fun fromName(name: String?): RecipeCategory? {
            return allCategories.find { it.name.equals(name, ignoreCase = true) }
        }
    }
}


