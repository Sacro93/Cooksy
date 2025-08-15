package com.example.cooksy.data.remote.openai

import com.google.gson.annotations.SerializedName

data class ChatCompletionResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("object") val objectType: String?,
    @SerializedName("created") val created: Long?,
    @SerializedName("model") val model: String?,
    @SerializedName("choices") val choices: List<ChoiceDto>?,
    @SerializedName("usage") val usage: UsageDto?
)
