package com.example.cooksy.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://api.spoonacular.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RecipeService by lazy {
        retrofit.create(RecipeService::class.java)
    }
}
/*✅ Separar la construcción del retrofit (mejor mantenimiento si en el futuro agregamos interceptores, logs, etc).*/