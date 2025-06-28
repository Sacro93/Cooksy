package com.example.cooksy

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.cooksy.data.remote.RetrofitInstance
import com.example.cooksy.data.repository.AuthRepository
import com.example.cooksy.data.repository.RecipeRepository
import com.example.cooksy.presentation.navigation.AppNavGraph
import com.example.cooksy.presentation.navigation.Routes
import com.example.cooksy.ui.theme.CooksyTheme
import com.example.cooksy.viewModel.SessionViewModel
import com.example.cooksy.viewModel.SessionViewModelFactory
import com.example.cooksy.viewModel.place.PlaceViewModel
import com.example.cooksy.viewModel.place.PlaceViewModelFactory
import com.example.cooksy.viewModel.recipe.RecipeViewModel
import com.example.cooksy.viewModel.recipe.RecipeViewModelFactory
import com.example.cooksy.viewModel.viral.ViralRecipeViewModel
import com.example.cooksy.viewModel.viral.ViralRecipeViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiService = RetrofitInstance.api
        val recipeRepository = RecipeRepository(apiService)
        val recipeFactory = RecipeViewModelFactory(recipeRepository)
        val recipeViewModel = ViewModelProvider(this, recipeFactory)[RecipeViewModel::class.java]

        val viralFactory = ViralRecipeViewModelFactory(applicationContext)
        val viralRecipeViewModel = ViewModelProvider(this, viralFactory)[ViralRecipeViewModel::class.java]

        val placeFactory = PlaceViewModelFactory(applicationContext)
        val placeViewModel = ViewModelProvider(this, placeFactory)[PlaceViewModel::class.java]

        val authRepository = AuthRepository()
        val sessionFactory = SessionViewModelFactory(authRepository)
        val sessionViewModel = ViewModelProvider(this, sessionFactory)[SessionViewModel::class.java]

        val startDestination = if (sessionViewModel.isUserLoggedIn) Routes.HOME else Routes.LOGIN

        setContent {
            CooksyTheme {
                SideEffect {
                    window.statusBarColor = Color.TRANSPARENT
                    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    AppNavGraph(
                        navController = navController,
                        recipeViewModel = recipeViewModel,
                        viralRecipeViewModel = viralRecipeViewModel,
                        sessionViewModel = sessionViewModel,
                        placeViewModel = placeViewModel
                    )
                }
            }
        }
    }
}

