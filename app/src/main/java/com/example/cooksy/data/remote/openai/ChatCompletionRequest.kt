package com.example.cooksy.data.remote.openai

import com.google.gson.annotations.SerializedName

data class ChatCompletionRequest(
    @SerializedName("model") val model: String = "gpt-3.5-turbo", // O el modelo que prefieras y tengas acceso
    @SerializedName("messages") val messages: List<ChatMessageDto>,
    @SerializedName("temperature") val temperature: Float = 0.7f, // Controla la aleatoriedad, 0.2 es más determinista, 0.8 más creativo
    @SerializedName("max_tokens") val maxTokens: Int = 1000 // Máximo de tokens a generar en la respuesta
    // Puedes añadir más parámetros según la documentación de OpenAI si los necesitas (e.g., n, stream, stop, etc.)
)