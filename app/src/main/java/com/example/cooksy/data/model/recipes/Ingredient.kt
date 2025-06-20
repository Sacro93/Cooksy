package com.example.cooksy.data.model.recipes
import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("original") val original: String
)
