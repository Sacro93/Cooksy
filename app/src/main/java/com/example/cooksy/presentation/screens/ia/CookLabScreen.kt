package com.example.cooksy.presentation.screens.ia

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CookLabScreen() {
    // TODO: Chat con IA para sugerencias de recetas
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("CookLab Screen", style = MaterialTheme.typography.titleLarge)
        // Aquí irá el chat IA con los ingredientes
    }
}

@Preview(showBackground = true)
@Composable
fun CookLabScreenPreview() {
    CookLabScreen()
}
