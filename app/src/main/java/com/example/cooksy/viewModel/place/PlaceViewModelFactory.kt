package com.example.cooksy.viewModel.place

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cooksy.data.repository.PlaceRepository
import com.example.cooksy.data.storage.PlaceStorage

class PlaceViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val storage = PlaceStorage(context.applicationContext)
        val repository = PlaceRepository(storage)
        return PlaceViewModel(repository) as T
    }
}