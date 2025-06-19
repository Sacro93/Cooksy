package com.example.cooksy.presentation.screens.recipes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import coil.compose.rememberAsyncImagePainter
import com.example.cooksy.data.model.Recipe

@Composable
fun RecipeDetailContent(recipe: Recipe, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
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
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Info rápida
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("⏱️ ${recipe.readyInMinutes} min", style = MaterialTheme.typography.bodyMedium)
            Text("🍽️ ${recipe.servings} servings", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ingredientes
        Text(
            "🧾 Ingredientes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (!recipe.ingredients.isNullOrEmpty()) {
            recipe.ingredients.forEach { ingredient ->
                Text("• ${ingredient.original}", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            Text("No se encontraron ingredientes.")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Instrucciones
        Text(
            "📋 Instrucciones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        val instructions = HtmlCompat.fromHtml(
            recipe.instructions ?: "No hay instrucciones disponibles.",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()

        Text(
            text = instructions,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón futuro
        OutlinedButton(
            onClick = { /* TODO: Translate */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Translate, contentDescription = "Translate")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Traducir al español")
        }
    }
}
