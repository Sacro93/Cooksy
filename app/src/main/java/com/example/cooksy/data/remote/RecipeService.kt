package com.example.cooksy.data.remote


import com.example.cooksy.data.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

//Offset para scroll infinito = desactivado

//Retrofit interface
interface RecipeService {
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("type") type: String? = null,
        // @Query("offset") offset: Int = 0,
        @Query("addRecipeInformation") addInfo: Boolean = true,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String = ApiConfig.API_KEY
    ): RecipeResponse
}
