package com.example.cooksy.presentation.screens.place
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cooksy.R
import com.example.cooksy.data.model.place.Place
import com.example.cooksy.data.model.place.PlaceCategory
import com.example.cooksy.viewModel.place.PlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlaceScreen(
    navController: NavHostController,
    viewModel: PlaceViewModel
) {
    var title by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf("instagram") }
    var selectedCategory by remember { mutableStateOf<PlaceCategory?>(null) }

    val platforms = listOf("instagram", "tiktok", "youtube")
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Lugar") },
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
                label = { Text("Nombre del lugar") },
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
                platforms.forEach { platform ->
                    val imageRes = when (platform.lowercase()) {
                        "instagram" -> R.drawable.instagram
                        "tiktok" -> R.drawable.tiktok
                        "youtube" -> R.drawable.youtube
                        else -> R.drawable.ic_launcher_foreground
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
                                contentDescription = platform,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Text(platform.replaceFirstChar { it.uppercase() })
                    }
                }
            }

            Text("Selecciona la categoría:", style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                PlaceCategory.entries.forEach { category ->
                    OutlinedButton(
                        onClick = { selectedCategory = category },
                        border = BorderStroke(
                            1.dp,
                            if (selectedCategory == category) Color.Blue else Color.Gray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedCategory == category) Color(0xFFE3F2FD) else Color.Transparent
                        )
                    ) {
                        Text(category.displayName)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && url.isNotBlank() && selectedCategory != null) {
                        viewModel.addPlace(
                            Place(
                                title = title,
                                url = url,
                                platform = selectedPlatform,
                                category = selectedCategory!!
                            )
                        )
                        navController.popBackStack()
                    }
                    else {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Lugar")
            }
        }
    }
}
