package com.example.cooksy.viewModel.viral

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cooksy.data.repository.ViralRecipeRepository
import com.example.cooksy.data.storage.ViralRecipeStorage

class ViralRecipeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val storage = ViralRecipeStorage(context.applicationContext)
        val repository = ViralRecipeRepository(storage)
        return ViralRecipeViewModel(repository) as T
    }
}
