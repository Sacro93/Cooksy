package com.example.cooksy.data.model.ia

import com.example.cooksy.data.model.recipes.Recipe // Importa tu modelo Recipe existente
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUserMessage: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    // Este campo es útil si quieres asociar directamente una receta completa
    // con un mensaje del asistente.
    val recipeSuggestion: Recipe? = null
)
