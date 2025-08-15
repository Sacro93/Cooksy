package com.example.cooksy.data.model.supermarket

import java.util.UUID

data class SupermarketItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    var isChecked: Boolean = false,
    var price: Double? = null,
    var quantity: String = "1",
    val dateAdded: Long = System.currentTimeMillis(),
    val userId: String = ""
)