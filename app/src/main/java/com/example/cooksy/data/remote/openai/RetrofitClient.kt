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

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val authInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        if (!BuildConfig.OPENAI_API_KEY.startsWith("YOUR_DEFAULT")) {
            requestBuilder.addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
        }
        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(OPENAI_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val openAiApiService: OpenAiApiService by lazy {
        retrofit.create(OpenAiApiService::class.java)
    }
}