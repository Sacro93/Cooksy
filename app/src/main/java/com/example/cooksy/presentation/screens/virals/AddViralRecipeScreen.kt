package com.example.cooksy.presentation.screens.virals
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.cooksy.data.model.viralRecipes.ViralRecipe
import com.example.cooksy.viewModel.viral.ViralRecipeViewModel
import com.example.cooksy.R
import com.example.cooksy.data.model.viralRecipes.Platform


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddViralRecipeScreen(
    navController: NavHostController,
    viewModel: ViralRecipeViewModel
) {
    var title by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf(Platform.INSTAGRAM) }
    var selectedCategory by remember { mutableStateOf("dulce") }

    val categorias = listOf("dulce", "salado", "agridulce")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Receta Viral") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Enlace") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Selecciona la plataforma:", style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Platform.entries.forEach { platform ->
                    val imageRes = when (platform) {
                        Platform.INSTAGRAM -> R.drawable.instagram
                        Platform.TIKTOK -> R.drawable.tiktok
                        Platform.YOUTUBE -> R.drawable.youtube
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selectedPlatform == platform) Color(0xFFBBDEFB) else Color.Transparent
                                )
                                .clickable { selectedPlatform = platform },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = platform.name,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Text(platform.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }

            Text("Selecciona el tipo de receta:", style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                categorias.forEach { categoria ->
                    OutlinedButton(
                        onClick = { selectedCategory = categoria },
                        border = BorderStroke(
                            1.dp,
                            if (selectedCategory == categoria) Color.Blue else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedCategory == categoria) Color(0xFFE3F2FD) else Color.Transparent
                        )
                    ) {
                        Text(categoria.replaceFirstChar { it.uppercase() })
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && url.isNotBlank()) {
                        viewModel.addRecipe(
                            ViralRecipe(
                                title = title,
                                url = url,
                                platform = selectedPlatform,
                                category = selectedCategory
                            )
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Receta")
            }
        }
    }
}
