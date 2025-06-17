package com.example.cooksy.presentation.screens.recipes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cooksy.data.model.Recipe

@Composable
fun RecipeDetailContent(recipe: Recipe) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Imagen principal
        Image(
            painter = rememberAsyncImagePainter(recipe.image),
            contentDescription = recipe.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tiempo de preparación y porciones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Ready in: ${recipe.readyInMinutes} min")
            Text("Servings: ${recipe.servings}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Ingredients", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))

        if (!recipe.ingredients.isNullOrEmpty()) {
            recipe.ingredients.forEach { ingredient ->
                Text("- ${ingredient.original}", modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp))
            }
        } else {
            Text("No ingredients available", modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp))
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Instrucciones
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(recipe.instructions ?: "No instructions available.")

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de traducción (para futuro)
        Button(
            onClick = { /* TODO: Translate */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Translate to Spanish")
        }
    }
}
