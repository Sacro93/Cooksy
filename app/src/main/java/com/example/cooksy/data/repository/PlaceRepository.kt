package com.example.cooksy.data.repository

import com.example.cooksy.data.model.place.Place
import com.example.cooksy.data.storage.PlaceStorage

class PlaceRepository(private val storage: PlaceStorage) {

    fun getAll(): List<Place> {
        return storage.loadPlaces()

    }

    fun add(place: Place) {
        val places = storage.loadPlaces().toMutableList()
        places.add(place)
        storage.savePlaces(places)
    }

    fun delete(place: Place) {
        val places = storage.loadPlaces().toMutableList()

    }
}