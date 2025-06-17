package com.example.cooksy.presentation.screens.recipes

import com.example.cooksy.data.model.Recipe


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cooksy.viewModel.RecipeViewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun RecipeListScreen(
    navController: NavHostController,
    viewModel: RecipeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is RecipeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is RecipeUiState.Success -> {
            val recipes = (uiState as RecipeUiState.Success).recipes

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(recipes) { recipe ->
                    RecipeItem(recipe = recipe, onClick = {
                        navController.navigate("detail/${recipe.id}")
                    })
                }
            }
        }
        is RecipeUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = (uiState as RecipeUiState.Error).message,
                    color = Color.Red
                )
            }
        }
    }
}





