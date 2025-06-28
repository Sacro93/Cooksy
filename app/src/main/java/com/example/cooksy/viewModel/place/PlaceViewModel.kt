package com.example.cooksy.viewModel.place

import androidx.lifecycle.ViewModel
import com.example.cooksy.data.model.place.Place
import com.example.cooksy.data.repository.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaceViewModel(private val repository: PlaceRepository): ViewModel()
{
    private val _places= MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places.asStateFlow()

    init {
        loadPlaces()
    }

    fun loadPlaces(){
        _places.value=repository.getAll()
    }

    fun addPlace(place: Place){
        repository.add(place)
        loadPlaces()
    }

    fun deletePlace(place: Place){
        repository.delete(place)
        loadPlaces()

    }
}