package com.example.cooksy.data.model.recipes

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("results") val results: List<Recipe>
)