package com.example.cooksy.data.model

import com.google.gson.annotations.SerializedName


data class Recipe(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String,
    @SerializedName("readyInMinutes") val readyInMinutes: Int,
    @SerializedName("servings") val servings: Int,
    @SerializedName("instructions") val instructions: String?,
    @SerializedName("extendedIngredients") val ingredients: List<Ingredient>?
)