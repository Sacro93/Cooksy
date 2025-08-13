package com.example.cooksy.data.remote.openai



import com.example.cooksy.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val OPENAI_BASE_URL = "https://api.openai.com/"

    // Logger para ver las peticiones y respuestas (muy útil para depurar)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Puedes cambiar a .NONE en producción
    }
    private val authInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        // Añade la cabecera de autorización solo si la clave no es la de por defecto
        if (!BuildConfig.OPENAI_API_KEY.startsWith("YOUR_DEFAULT")) {
            requestBuilder.addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
        }
        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS) // Aumenta timeouts si es necesario
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(OPENAI_BASE_URL)
        .client(okHttpClient) // Usa el OkHttpClient configurado
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val openAiApiService: OpenAiApiService by lazy {
        retrofit.create(OpenAiApiService::class.java)
    }
}