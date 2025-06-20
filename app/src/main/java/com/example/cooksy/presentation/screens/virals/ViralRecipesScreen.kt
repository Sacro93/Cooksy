package com.example.cooksy.presentation.screens.virals


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cooksy.viewModel.viral.ViralRecipeViewModel
import androidx.core.net.toUri
import com.example.cooksy.R
import com.example.cooksy.data.model.viralRecipes.Platform
import com.example.cooksy.data.model.viralRecipes.ViralRecipe
import com.example.cooksy.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViralRecipesScreen(
    navController: NavHostController,
    viewModel: ViralRecipeViewModel
) {
    val context = LocalContext.current
    val recipes by viewModel.recipes.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var recipeToDelete by remember { mutableStateOf<ViralRecipe?>(null) }

    val backgroundBrush = Brush.verticalGradient(
        listOf(Color(0xFF512DA8), Color(0xFF1976D2))
    )

    val filteredRecipes = recipes.filter {
        it.title.contains(searchQuery, ignoreCase = true) &&
                (selectedCategory == null || it.category == selectedCategory)
    }

    val categorias = listOf("dulce", "salado", "agridulce")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recetas Virales") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF512DA8),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.ADD_VIRAL_RECIPE) },
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush)
                .padding(padding)
        ) {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar por título") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(start = 16.dp, bottom = 8.dp)
                ) {
                    categorias.forEach { categoria ->
                        val isSelected = selectedCategory == categoria
                        OutlinedButton(
                            onClick = {
                                selectedCategory = if (isSelected) null else categoria
                            },
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color.White else Color.Gray
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) Color(0xFF90CAF9) else Color.Transparent,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(categoria.replaceFirstChar { it.uppercase() })
                        }
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredRecipes) { recipe ->
                        val icon = when (recipe.platform) {
                            Platform.INSTAGRAM -> R.drawable.instagram
                            Platform.TIKTOK -> R.drawable.tiktok
                            Platform.YOUTUBE -> R.drawable.youtube
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = icon),
                                    contentDescription = recipe.platform.name,
                                    modifier = Modifier.size(40.dp)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_VIEW, recipe.url.toUri())
                                            context.startActivity(intent)
                                        }
                                ) {
                                    Text(
                                        text = recipe.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Black
                                    )
                                    recipe.category?.let {
                                        Text(
                                            text = it.replaceFirstChar { c -> c.uppercase() },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        recipeToDelete = recipe
                                        showDialog = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showDialog && recipeToDelete != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Eliminar receta") },
                    text = { Text("¿Estás seguro de que deseas eliminar esta receta?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteRecipe(recipeToDelete!!)
                                showDialog = false
                            }
                        ) {
                            Text("Eliminar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
