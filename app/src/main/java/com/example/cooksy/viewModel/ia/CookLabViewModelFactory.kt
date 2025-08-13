package com.example.cooksy.viewModel.ia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cooksy.data.repository.CookLabRepository
import com.example.cooksy.data.repository.CookLabRepositoryImpl

/**
 * Factory for creating [CookLabViewModel] with a [CookLabRepository] dependency.
 */
class CookLabViewModelFactory(
    private val repository: CookLabRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CookLabViewModel::class.java)) {
            return CookLabViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

/**
 * Helper function to easily provide an instance of [CookLabViewModelFactory].
 * This is a simple approach. In a larger app, consider using a dependency injection framework like Hilt.
 */
fun provideCookLabViewModelFactory(): CookLabViewModelFactory {
    // Creates a new instance of the repository. For more complex scenarios or testing,
    // this repository instance should be managed by a DI framework.
    val repository: CookLabRepository = CookLabRepositoryImpl()
    return CookLabViewModelFactory(repository)
}
