package com.example.cooksy.presentation.screens.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cooksy.viewModel.recipe.RecipeViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.cooksy.data.model.recipes.RecipeCategory
import com.example.cooksy.presentation.navigation.Routes

@Composable
fun RecipeListScreen(
    navController: NavHostController,
    categoryName: String?,
    viewModel: RecipeViewModel
) {
    val category = RecipeCategory.allCategories.find { it.name == categoryName }

    LaunchedEffect(category) {
        category?.let { viewModel.loadRecipesByCategory(it.name) }
    }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 30.dp, end = 8.dp, bottom = 0.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            category?.let {
                Text(
                    text = "Recetas de ${it.name.uppercase()}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Contenido principal
        when (uiState) {
            is RecipeUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is RecipeUiState.Success -> {
                val recipes = (uiState as RecipeUiState.Success).recipes

                if (recipes.isEmpty()) {
                    Text("No se encontraron recetas", modifier = Modifier.padding(16.dp))
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recipes) { recipe ->
                            RecipeItem(recipe = recipe) {
                                navController.navigate(Routes.recipeDetail(recipe.id))
                            }
                        }
                    }
                }
            }

            is RecipeUiState.Error -> {
                Text(
                    text = (uiState as RecipeUiState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> Text("Selecciona una categoría", modifier = Modifier.padding(16.dp))
        }
    }
}
