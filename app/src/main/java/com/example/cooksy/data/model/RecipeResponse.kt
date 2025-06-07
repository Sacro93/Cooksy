package com.example.cooksy.data.model

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("recipes") val recipes: List<Recipe>
)