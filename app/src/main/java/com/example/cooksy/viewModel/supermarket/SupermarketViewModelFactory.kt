package com.example.cooksy.viewModel.supermarket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cooksy.data.repository.AuthRepository

class SupermarketViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SupermarketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SupermarketViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
