package com.example.cooksy.presentation.screens.place

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.cooksy.viewModel.place.PlaceViewModel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.cooksy.R
import com.example.cooksy.data.model.place.Place
import com.example.cooksy.data.model.place.PlaceCategory
import com.example.cooksy.presentation.navigation.Routes

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PlaceScreen(
    navController: NavHostController,
    viewModel: PlaceViewModel

) {

    val backgroundBrush = Brush.verticalGradient(
        listOf(Color(0xFF512DA8), Color(0xFF1976D2))
    )

    val context = LocalContext.current
    val places by viewModel.places.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var placeToDelete by remember { mutableStateOf<Place?>(null) }
    val categories = PlaceCategory.entries.toTypedArray()
    var selectedCategory by remember { mutableStateOf<PlaceCategory?>(null) }

    val filteredPlaces = places.filter {
        it.title.contains(searchQuery, ignoreCase = true) &&
                (selectedCategory == null || it.category == selectedCategory)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lugares") },
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
                onClick = { navController.navigate(Routes.ADD_PLACE) },
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush)
                .padding(padding)
        ) {
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
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    OutlinedButton(
                        onClick = {
                            selectedCategory = if (isSelected) null else category
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
                        Text(category.displayName)
                    }
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredPlaces) { place ->
                    val icon = when (place.platform.lowercase()) {
                        "instagram" -> R.drawable.instagram
                        "tiktok" -> R.drawable.tiktok
                        "youtube" -> R.drawable.youtube
                        else -> R.drawable.ic_launcher_foreground // ícono por defecto
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
                                contentDescription = place.platform,
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, place.url.toUri())
                                        context.startActivity(intent)
                                    }
                            ) {
                                Text(
                                    text = place.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Black
                                )
                                Text(
                                    text = place.category.displayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }

                            IconButton(
                                onClick = {
                                    placeToDelete = place
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
            if (showDialog && placeToDelete != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Eliminar lugar") },
                    text = { Text("¿Estás seguro de que deseas eliminar este lugar?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deletePlace(placeToDelete!!)
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
