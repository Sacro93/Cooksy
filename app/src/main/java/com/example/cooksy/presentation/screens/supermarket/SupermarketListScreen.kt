package com.example.cooksy.presentation.screens.supermarket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cooksy.data.model.supermarket.SupermarketItem
import com.example.cooksy.presentation.components.SupermarketListItem
import com.example.cooksy.ui.theme.CooksyTheme
import com.example.cooksy.viewModel.supermarket.SupermarketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupermarketListScreen(
    navController: NavHostController,
    viewModel: SupermarketViewModel
) {
    val supermarketItems by viewModel.supermarketItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Initialize by loading items (if not already loaded)
    // LaunchedEffect(Unit) {
    //     if (supermarketItems.isEmpty()) { // Or a more sophisticated check
    //         viewModel.loadSupermarketItems()
    //     }
    // }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Supermercado") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // TODO: Navigate to an "Add Item" screen
                // navController.navigate(Routes.ADD_SUPERMARKET_ITEM) // You'll need to define this route
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Item")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (isLoading && supermarketItems.isEmpty()) { // Show full screen loading only if list is empty
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (supermarketItems.isEmpty()) {
                Text(
                    text = "Tu lista de supermercado está vacía. Añade algunos artículos!",
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(supermarketItems, key = { it.id }) { item ->
                        SupermarketListItem(
                            item = item,
                            onCheckedChange = { viewModel.toggleItemChecked(item.id, it) },
                            onItemClick = {
                                // TODO: Navigate to an "Edit Item" screen, passing item.id
                                // navController.navigate("${Routes.EDIT_SUPERMARKET_ITEM}/${item.id}") // Define this route
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun SupermarketListScreenPreview() {
    val mockNavController = rememberNavController()
    // It's better to instantiate ViewModel outside Composable if it involves logic,
    // but for simple preview, this can be acceptable.
    val mockViewModel: SupermarketViewModel = viewModel()

    // Add mock data for a more realistic preview
    // This part should ideally be in a way that doesn't trigger recomposition loops
    // if `addItem` modifies state observed by `collectAsState` directly in preview.
    // For simple previews of UI structure, you might pass a static list.
    // However, SupermarketViewModel is designed to be stateful.

    // Example of how you might add items for preview if ViewModel supports it cleanly:
    // if (mockViewModel.supermarketItems.collectAsState().value.isEmpty()) {
    //     mockViewModel.addItem("Milk", "1 Gallon", 3.50)
    //     mockViewModel.addItem("Bread", "1 Loaf", 2.00)
    //     mockViewModel.addItem("Eggs", "1 Dozen", 2.79)
    //     // Simulate one item being checked
    //     // val items = mockViewModel.supermarketItems.value
    //     // if (items.isNotEmpty()) {
    //     //     mockViewModel.toggleItemChecked(items.first().id, true)
    //     // }
    // }


    CooksyTheme {
        SupermarketListScreen(
            navController = mockNavController,
            viewModel = mockViewModel
        )
    }
}

@Preview(showBackground = true, name = "List Item Preview")
@Composable
fun SupermarketListItemPreview() {
    CooksyTheme {
        Column {
            SupermarketListItem(
                item = SupermarketItem(name = "Sample Item", quantity = "2", price = 10.99, isChecked = false, dateAdded = System.currentTimeMillis()),
                onCheckedChange = {},
                onItemClick = {}
            )
            SupermarketListItem(
                item = SupermarketItem(name = "Checked Item", quantity = "1 kg", price = 5.45, isChecked = true, dateAdded = System.currentTimeMillis() - 86400000), // Yesterday
                onCheckedChange = {},
                onItemClick = {}
            )
        }
    }
}
