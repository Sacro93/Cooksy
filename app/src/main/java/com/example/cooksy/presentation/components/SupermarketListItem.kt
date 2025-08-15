package com.example.cooksy.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cooksy.data.model.supermarket.SupermarketItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun SupermarketListItem(
    item: SupermarketItem,
    onCheckedChange: (Boolean) -> Unit,
    onItemClick: () -> Unit // For navigating to edit or view details
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isChecked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (item.isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
                    color = if (item.isChecked) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cant: ${item.quantity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (item.isChecked) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (item.dateAdded != 0L) { // Avoid showing default date if not set
                        Text(
                            text = "Añadido: ${dateFormat.format(Date(item.dateAdded))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (item.isChecked) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            if (item.price != null && item.price!! > 0) { // Only show price if set and greater than 0
                Text(
                    text = "$${String.format(Locale.getDefault(), "%.2f", item.price)}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (item.isChecked) Color.Gray else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
