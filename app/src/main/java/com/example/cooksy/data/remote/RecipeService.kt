package com.example.cooksy.data.remote


import com.example.cooksy.data.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query


//Retrofit interface
interface RecipeService {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 10,
        @Query("tags") tags: String = "",
        @Query("apiKey") apiKey: String
    ): RecipeResponse
}
