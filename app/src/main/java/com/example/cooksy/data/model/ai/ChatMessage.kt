package com.example.cooksy.data.model.ai


import com.example.cooksy.data.model.recipes.Recipe
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUserMessage: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val recipeSuggestion: Recipe? = null
)