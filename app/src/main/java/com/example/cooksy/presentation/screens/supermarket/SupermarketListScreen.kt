package com.example.cooksy.presentation.screens.supermarket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SupermarketListScreen() {
    // TODO: UI para la lista del supermercado
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Supermarket List Screen", style = MaterialTheme.typography.titleLarge)
        // Aquí irán tus notas y productos
    }
}

@Preview(showBackground = true)
@Composable
fun SupermarketListScreenPreview() {
    SupermarketListScreen()
}
