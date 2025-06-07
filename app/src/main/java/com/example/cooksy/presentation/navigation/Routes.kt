package com.example.cooksy.presentation.navigation

object Routes {

    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val RECIPE_LIST = "list"
    const val RECIPE_DETAIL = "detail/{recipeId}"
    const val VIRAL_RECIPES = "viral_recipes"
    const val SUPERMARKET_LIST = "supermarket_list"
    const val FAVOURITES = "favourites"
    const val PROFILE = "profile"
    const val COOK_LAB = "cook_lab"

    fun recipeDetail(recipeId: Int) = "detail/$recipeId"
}
