package com.example.cooksy.viewModel.ia

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cooksy.data.model.ia.ChatMessage
import com.example.cooksy.data.model.recipes.Recipe
import com.example.cooksy.data.repository.CookLabRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class CookLabViewModel(
    private val repository: CookLabRepository
) : ViewModel() {

    // Private mutable state for user input
    private val _userInput = mutableStateOf("")
    // Public immutable state for UI to observe
    val userInput: MutableState<String> = _userInput

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Add an initial greeting message from the AI
        _messages.value = listOf(
            ChatMessage(
                text = "Hello! What ingredients do you have today? I can suggest some recipes!",
                isUserMessage = false
            )
        )
    }

    fun onUserInputChanged(newInput: String) {
        _userInput.value = newInput
    }

    fun sendMessage() {
        val currentInput = _userInput.value.trim()
        if (currentInput.isBlank()) {
            return
        }

        // Add user message to the list
        val userMessage = ChatMessage(text = currentInput, isUserMessage = true)
        _messages.value = _messages.value + userMessage

        // Clear the input field
        _userInput.value = ""

        // Process the input for ingredients (simple comma separation for now)
        val ingredientsList = currentInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }

        if (ingredientsList.isNotEmpty()) {
            fetchRecipeSuggestions(ingredientsList)
        } else {
            // If input is not empty but no valid ingredients found (e.g., just spaces after trim)
            val clarificationMessage = ChatMessage(
                text = "I couldn't quite catch those ingredients. Could you list them, perhaps separated by commas?",
                isUserMessage = false
            )
            _messages.value = _messages.value + clarificationMessage
        }
    }

    private fun fetchRecipeSuggestions(ingredients: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            var thinkingMessage: ChatMessage? = null // To hold the "thinking" message for later removal

            // Optional: Add a "thinking" message from AI
            // Ensure this message has a unique ID if you plan to remove/update it.
             thinkingMessage = ChatMessage(
                text = "Let me think about what you can make with: ${ingredients.joinToString(", ")}...",
                isUserMessage = false
            )
            _messages.value = _messages.value + thinkingMessage

            Timber.d("Fetching recipes for: $ingredients")

            repository.getRecipeSuggestions(ingredients)
                .onSuccess { recipes ->
                    Timber.d("Successfully fetched recipes: ${recipes.size}")
                    // Remove the "thinking" message before adding new recipes or "no recipes" message
                    if (thinkingMessage != null) {
                        _messages.value = _messages.value.filterNot { it.id == thinkingMessage.id }
                    }

                    if (recipes.isNotEmpty()) {
                        val recipeMessages = recipes.map { recipeToChatMessage(it) }
                        _messages.value = _messages.value + recipeMessages
                    } else {
                        val noRecipeMessage = ChatMessage(
                            text = "I couldn't find any specific recipes for those ingredients. Maybe try a broader search or different ingredients?",
                            isUserMessage = false
                        )
                        _messages.value = _messages.value + noRecipeMessage
                    }
                }
                .onFailure { error ->
                    Timber.e(error, "Error fetching recipes")
                    if (thinkingMessage != null) {
                        _messages.value = _messages.value.filterNot { it.id == thinkingMessage.id }
                    }
                    val errorMessage = ChatMessage(
                        text = "Sorry, I had a little trouble connecting to my recipe brain. Please check your connection or try again in a moment. Error: ${error.localizedMessage}",
                        isUserMessage = false
                    )
                    _messages.value = _messages.value + errorMessage
                }
            _isLoading.value = false
        }
    }

    private fun recipeToChatMessage(recipe: Recipe): ChatMessage {
        // Customize this to format the recipe details as you like for the chat.
        // This is a more detailed example.
        val ingredientsListString = recipe.ingredients?.joinToString(separator = "\n- ", prefix = "- ") { it.original } ?: "Not specified"
        val instructionsString = recipe.instructions?.replace("\n", "\n") ?: "Not specified" // Ensure newlines in instructions

        val recipeText = """
            Found a recipe for you: **${recipe.title}**

            **Ready in:** ${recipe.readyInMinutes} minutes
            **Serves:** ${recipe.servings}

            **Ingredients:**
            $ingredientsListString

            **Instructions:**
            ${instructionsString.lines().joinToString(separator = "\n") { it.trim() }}
        """.trimIndent() // trimIndent is important for multi-line strings

        return ChatMessage(
            text = recipeText,
            isUserMessage = false,
            recipeSuggestion = recipe // Keep the original recipe object if needed later
        )
    }
}
