package com.example.cooksy.presentation.screens.recipes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.cooksy.viewModel.recipe.RecipeViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cooksy.data.model.recipes.RecipeUiState
import com.example.cooksy.presentation.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeSearchScreen(
    navController: NavHostController,
    viewModel: RecipeViewModel
) {
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Buscar Recetas", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1565C0))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar receta...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {
                        if (query.isNotBlank()) {
                            viewModel.searchRecipes(query)
                        } else {
                            Toast.makeText(context, "Escribí algo para buscar", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is RecipeUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is RecipeUiState.Success -> {
                    val recipes = (uiState as RecipeUiState.Success).recipes
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(recipes) { recipe ->
                            RecipeItem(recipe = recipe, onClick = {
                                navController.navigate(Routes.recipeDetail(recipe.id))
                            })
                        }
                    }
                }
                is RecipeUiState.Error -> {
                    Text("No se pudieron cargar recetas.", color = Color.Red)
                }

            }
        }
    }
}
