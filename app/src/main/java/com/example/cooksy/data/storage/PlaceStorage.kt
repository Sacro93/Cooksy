package com.example.cooksy.data.storage

import android.content.Context
import com.example.cooksy.data.model.place.Place
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

class PlaceStorage(private val context: Context) {

    private val fileName = "place.json"
    private val json = Json { prettyPrint = true }

    fun savePlaces(places: List<Place>) {
        val file = File(context.filesDir, fileName)
        file.writeText(json.encodeToString(ListSerializer(Place.serializer()), places))
    }

    fun loadPlaces(): List<Place> {
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            try {
                json.decodeFromString(ListSerializer(Place.serializer()), file.readText())
            } catch (e: Exception) {
                emptyList()
            }
        } else emptyList()
    }
}