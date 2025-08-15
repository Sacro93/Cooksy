package com.example.cooksy.data.remote.openai

import com.google.gson.annotations.SerializedName
/*
* El ChatMessageDto que se define en OpenAiApiModels.kt es un Modelo de
*  Transferencia de Datos (DTO) específico para la estructura que la API de OpenAI
*  espera en sus solicitudes y devuelve en sus respuestas.*/
data class ChatMessageDto(
    @SerializedName("role") val role: String, // "system", "user", or "assistant"
    @SerializedName("content") val content: String
)