package com.example.cooksy.presentation.screens.ia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
// Remove Preview import if not used, or keep for simplified previews
// import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cooksy.data.model.ia.ChatMessage // Import your ChatMessage
import com.example.cooksy.viewModel.ia.CookLabViewModel
import com.example.cooksy.viewModel.ia.provideCookLabViewModelFactory
import kotlinx.coroutines.launch

// PlaceholderMessage data class is removed as we now use com.example.cooksy.data.model.ia.ChatMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookLabScreen(viewModel: CookLabViewModel
) {

    val messages by viewModel.messages.collectAsState()
    val userInput by viewModel.userInput
    val isLoading by viewModel.isLoading.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CookLab AI Assistant") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            MessageInputRow(
                userInput = userInput,
                onUserInputChanged = { viewModel.onUserInputChanged(it) },
                onSendMessage = { viewModel.sendMessage() },
                isLoading = isLoading
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f) // Takes up all available space above the input field
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) } // Spacer at the top

                items(messages, key = { it.id }) { message ->
                    MessageBubble(
                        text = message.text,
                        isUserMessage = message.isUserMessage,
                        // You could pass the full message object if MessageBubble needs more info later
                        // message = message 
                    )
                }
                 // Optional: Show a "typing" indicator if isLoading and it's not the initial load
                if (isLoading && messages.any { it.isUserMessage }) { // Show only if user has sent something
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                             Row(
                                modifier = Modifier.align(Alignment.CenterStart), // Align to left for AI "typing"
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "CookLab is thinking...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) } // Spacer at the bottom
            }
        }
    }
}

@Composable
fun MessageInputRow(
    userInput: String,
    onUserInputChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = userInput,
            onValueChange = onUserInputChanged,
            label = { Text("Type your ingredients...") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            enabled = !isLoading, // Disable input while AI is processing
            trailingIcon = {
                if (isLoading && userInput.isEmpty()) { // Show loader in text field if user input is empty and loading
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }
            }
        )
        IconButton(
            onClick = onSendMessage,
            modifier = Modifier.padding(start = 8.dp),
            enabled = userInput.isNotBlank() && !isLoading // Disable send if input is blank or AI is processing
        ) {
            Icon(
                Icons.Filled.Send,
                contentDescription = "Send message",
                tint = if (userInput.isNotBlank() && !isLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }
    }
}

@Composable
fun MessageBubble(text: String, isUserMessage: Boolean) { // Kept your original MessageBubble
    val alignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isUserMessage) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    val textColor = if (isUserMessage) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (isUserMessage) 64.dp else 0.dp,
                end = if (isUserMessage) 0.dp else 64.dp
            )
    ) {
        Text(
            text = text,
            modifier = Modifier
                .align(alignment)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUserMessage) 16.dp else 0.dp,
                        bottomEnd = if (isUserMessage) 0.dp else 16.dp
                    )
                )
                .background(backgroundColor)
                .padding(12.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}

// Previews would need to be adjusted or removed as they might not work with ViewModel dependencies
// For example, a simple preview for MessageBubble can still exist:
/*
@Preview(showBackground = true, name = "User Message Bubble")
@Composable
fun UserMessageBubblePreview() {
    // Wrap in a theme if you have one: YourAppTheme { ... }
    Box(modifier = Modifier.padding(8.dp)) {
        MessageBubble(text = "I have chicken and potatoes.", isUserMessage = true)
    }
}

@Preview(showBackground = true, name = "AI Message Bubble")
@Composable
fun AiMessageBubblePreview() {
    // Wrap in a theme if you have one: YourAppTheme { ... }
    Box(modifier = Modifier.padding(8.dp)) {
        MessageBubble(text = "Okay, you could make roasted chicken and potatoes!", isUserMessage = false)
    }
}
*/
