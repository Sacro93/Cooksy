package com.example.cooksy.data.model.place

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Place(
    val id: String = UUID.randomUUID().toString(),
    val title:String,
    val url:String,
    val platform: String,
    val category: PlaceCategory

)

@Serializable
enum class PlaceCategory(val displayName: String) {
    CAFETERIA("Cafetería"),
    HELADERIA("Heladería"),
    RESTAURANTE("Restaurante"),
    PASTELERIA("Pastelería")
}
