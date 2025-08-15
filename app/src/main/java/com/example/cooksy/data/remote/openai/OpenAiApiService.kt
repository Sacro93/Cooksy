package com.example.cooksy.data.remote.openai

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiApiService {

    @POST("v1/chat/completions") // Endpoint para Chat Completions
    suspend fun getChatCompletions(
        @Body requestBody: ChatCompletionRequest
    ): Response<ChatCompletionResponse> // Usa Response<T> para obtener la respuesta completa incluyendo códigos de estado
}