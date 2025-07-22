package com.example.cooksy.presentation.screens.recipes


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cooksy.viewModel.recipe.RecipeViewModel
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.cooksy.data.model.recipes.RecipeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    navController: NavHostController,
    viewModel: RecipeViewModel
) {
    LaunchedEffect(recipeId) {
        viewModel.loadRecipeDetail(recipeId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Receta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is RecipeUiState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is RecipeUiState.Success -> {
                val recipe = (uiState as RecipeUiState.Success).recipes.firstOrNull()
                recipe?.let {
                    RecipeDetailContent(it, Modifier.padding(padding))
                } ?: Text("Recipe not found", modifier = Modifier.padding(padding))
            }

            is RecipeUiState.Error -> {
                Text(
                    (uiState as RecipeUiState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}
