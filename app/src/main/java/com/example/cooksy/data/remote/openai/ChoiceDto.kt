package com.example.cooksy.data.remote.openai

import com.google.gson.annotations.SerializedName


data class ChoiceDto(
    @SerializedName("index") val index: Int?,
    @SerializedName("message") val message: ChatMessageDto?, // La respuesta del asistente
    @SerializedName("finish_reason") val finishReason: String? // e.g., "stop", "length"
)