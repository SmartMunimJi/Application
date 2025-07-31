// app/src/main/java/com/project/smartmunimji/network/RetrofitClient.kt

package com.project.smartmunimji.network

import com.project.smartmunimji.utils.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Base URL for your Smart Munim Ji backend APIs
    // IMPORTANT:
    // 1. For EMULATOR: use "http://10.0.2.2:3000/sm/" to connect to host machine's localhost.
    // 2. For PHYSICAL DEVICE: Replace "YOUR_HOST_MACHINE_LOCAL_IP" with your actual local IP (e.g., 192.168.1.5).
    //    Your device and host machine MUST be on the same Wi-Fi network.
    private const val BASE_URL = "http://10.0.2.2:3000/sm/" // <--- *** CHANGE THIS FOR PHYSICAL DEVICE ***

    // Using a private variable with a public getter to ensure controlled access and singleton pattern
    @Volatile
    private var apiServiceInstance: ApiService? = null

    // Public method to get the ApiService instance
    fun getApiService(tokenManager: TokenManager): ApiService {
        return apiServiceInstance ?: synchronized(this) {
            // Double-check inside synchronized block
            apiServiceInstance ?: buildRetrofit(tokenManager).also {
                apiServiceInstance = it // Assign the built instance
            }
        }
    }

    private fun buildRetrofit(tokenManager: TokenManager): ApiService {
        // Logging Interceptor for debugging network requests/responses
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request and response bodies
        }

        // OkHttpClient with interceptors
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager)) // Add AuthInterceptor for JWT
            .addInterceptor(loggingInterceptor) // Add logging interceptor
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
            .build()

        // Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON parsing
            .build()

        return retrofit.create(ApiService::class.java)
    }
}