package com.example.cooksy.data.remote.openai

import com.google.gson.annotations.SerializedName

data class UsageDto(
    @SerializedName("prompt_tokens") val promptTokens: Int?,
    @SerializedName("completion_tokens") val completionTokens: Int?,
    @SerializedName("total_tokens") val totalTokens: Int?
)