package com.example.cooksy.presentation.navigation


object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"

    const val CATEGORY_SELECTOR = "category_selector"
    const val RECIPE_LIST_BY_CATEGORY = "list/{categoryName}"
    const val RECIPE_DETAIL = "detail/{recipeId}"

    const val VIRAL_RECIPES = "viral_recipes"
    const val ADD_VIRAL_RECIPE = "add_viral_recipe"

    const val SUPERMARKET_LIST = "supermarket_list"
    const val FAVOURITES = "favourites"
    const val PROFILE = "profile"
    const val COOK_LAB = "cook_lab"

    fun recipeDetail(recipeId: Int) = "detail/$recipeId"
    fun recipeListByCategory(categoryName: String) = "list/$categoryName"
}
