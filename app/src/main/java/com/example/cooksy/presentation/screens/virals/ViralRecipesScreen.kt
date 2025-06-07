package com.example.cooksy.presentation.screens.virals


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
fun ViralRecipesScreen() {
    // TODO: Mostrar recetas (API)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Recipe Detail Screen", style = MaterialTheme.typography.titleLarge)
        // Aquí vendrá la lista de recetas por categoría
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailScreenPreview() {
    ViralRecipesScreen()
}
