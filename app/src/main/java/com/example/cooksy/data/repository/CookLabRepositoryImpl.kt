package com.example.cooksy.data.repository

import com.example.cooksy.BuildConfig
import com.example.cooksy.data.model.recipes.Recipe
import com.example.cooksy.data.remote.openai.ChatCompletionRequest
import com.example.cooksy.data.remote.openai.ChatMessageDto
import com.example.cooksy.data.remote.openai.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CookLabRepositoryImpl : CookLabRepository {

    private val openAiService = RetrofitClient.openAiApiService
    private val gson = Gson()

    private val systemPrompt = """
    You are a culinary assistant. Your goal is to provide recipe suggestions.
    You MUST respond with a VALID JSON array of recipe objects. 
    Each recipe object must conform to the following structure:
    {
      "id": Int (e.g., 1),
      "title": String (e.g., "Tomato Pasta"),
      "image": String (URL or placeholder, e.g., "pasta.jpg"),
      "readyInMinutes": Int (e.g., 30),
      "servings": Int (e.g., 4),
      "instructions": String (step-by-step cooking instructions),
      "extendedIngredients": Array of objects, each like {"original": String (e.g., "1 cup flour")}
    }
    Do NOT include any text, explanations, or apologies before or after the JSON array.
    The entire response should be ONLY the JSON array, starting with '[' and ending with ']'.
    If no recipes can be found or if the ingredients are insufficient, return an empty JSON array [].
    """.trimIndent()

    // Template for the user's message, incorporating their ingredients.
    private fun getUserPrompt(ingredients: List<String>): String {
        return "Suggest recipes based on these ingredients: ${ingredients.joinToString(", ")}."
    }

    override suspend fun getRecipeSuggestions(userIngredientsText: List<String>): Result<List<Recipe>> {

        if (BuildConfig.OPENAI_API_KEY.startsWith("YOUR_DEFAULT")) {
            Timber.e("OpenAI API Key is a default placeholder. Please set it in local.properties.")
            return Result.failure(Exception("Invalid OpenAI API Key. Please configure it correctly."))
        }

        val messages = listOf(
            ChatMessageDto(role = "system", content = systemPrompt),
            ChatMessageDto(role = "user", content = getUserPrompt(userIngredientsText))
        )

        val request = ChatCompletionRequest(
            model = "gpt-3.5-turbo-0125",
            messages = messages,
            temperature = 0.5f,
            maxTokens = 2000,
        )

        return withContext(Dispatchers.IO) {
            try {
                Timber.d("Requesting recipe suggestions for: $userIngredientsText")
                val response = openAiService.getChatCompletions(request)

                if (response.isSuccessful) {
                    val chatResponse = response.body()
                    val assistantMessageContent = chatResponse?.choices?.firstOrNull()?.message?.content
                    Timber.d("Raw AI Response: $assistantMessageContent")

                    if (assistantMessageContent != null) {
                        parseRecipesFromJsonResponse(assistantMessageContent)
                    } else {
                        Timber.w("AI response content is null or empty.")
                        Result.failure(Exception("AI response content is empty or invalid."))
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown API error"
                    Timber.e("API Error: ${response.code()} - $errorBody")
                    Result.failure(Exception("API Error: ${response.code()} - $errorBody"))
                }
            } catch (e: Exception) {
                Timber.e(e, "Network or other error during AI request: ${e.message}")
                Result.failure(Exception("Network or other error: ${e.message}", e))
            }
        }
    }

    private fun parseRecipesFromJsonResponse(jsonString: String): Result<List<Recipe>> {
        return try {
            val pureJsonString = extractJsonArrayString(jsonString)
            if (pureJsonString.isBlank()) {
                Timber.w("Extracted JSON string is blank. Original: $jsonString")
                return Result.success(emptyList()) // Treat blank JSON as no recipes found
            }

            val recipeListType = object : TypeToken<List<Recipe>>() {}.type
            val recipes: List<Recipe> = gson.fromJson(pureJsonString, recipeListType)
            Timber.d("Successfully parsed ${recipes.size} recipes.")
            Result.success(recipes)
        } catch (e: JsonSyntaxException) {
            Timber.e(e, "Failed to parse JSON response from AI. Raw response: $jsonString")
            Result.failure(Exception("Failed to parse AI response: ${e.message}. Check logs for raw response.", e))
        } catch (e: Exception) {
            Timber.e(e, "Error processing AI response: ${e.message}. Raw response: $jsonString")
            Result.failure(Exception("Error processing AI response: ${e.message}", e))
        }
    }

    private fun extractJsonArrayString(rawString: String): String {
        val startIndex = rawString.indexOfFirst { it == '[' }
        val endIndex = rawString.indexOfLast { it == ']' }

        return if (startIndex != -1 && endIndex != -1 && endIndex >= startIndex) {
            rawString.substring(startIndex, endIndex + 1).trim()
        } else {
            Timber.w("Could not find a clear JSON array in the raw string. Using trimmed original.")
            rawString.trim()
        }
    }
}
