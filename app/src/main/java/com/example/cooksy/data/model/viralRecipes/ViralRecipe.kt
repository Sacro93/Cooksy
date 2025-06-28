package com.example.cooksy.data.model.viralRecipes

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ViralRecipe(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val url: String,
    val platform: Platform,
    val category: String? = null
)

@Serializable
enum class Platform {
    INSTAGRAM, TIKTOK, YOUTUBE
}
