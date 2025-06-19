package com.example.cooksy.data.remote


import com.example.cooksy.data.model.Recipe
import com.example.cooksy.data.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Offset para scroll infinito = desactivado

//Retrofit interface
interface RecipeService {

    //sirve para listar muchas recetas con datos básicos/intermedios.
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("type") type: String? = null,
        // @Query("offset") offset: Int = 0,
        @Query("addRecipeInformation") addInfo: Boolean = true,
        @Query("number") number: Int = 10,
        @Query("apiKey") apiKey: String = ApiConfig.API_KEY
    ): RecipeResponse

//sirve para obtener una receta en detalle, incluyendo instructions e ingredients.
    @GET("recipes/{id}/information")
    suspend fun getRecipeDetail(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = ApiConfig.API_KEY
    ): Recipe

}
