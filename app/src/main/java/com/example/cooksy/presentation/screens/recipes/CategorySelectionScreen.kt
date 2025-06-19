package com.example.cooksy.presentation.screens.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cooksy.data.model.RecipeCategory
import com.example.cooksy.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explorar por categoría") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(RecipeCategory.allCategories) { category ->
                Button(
                    onClick = {
                        navController.navigate(
                            Routes.recipeListByCategory(category.name)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(category.displayName)
                }

            }
        }
    }
}
