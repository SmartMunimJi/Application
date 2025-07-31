// app/src/main/java/com/project/smartmunimji/network/AuthInterceptor.kt

package com.project.smartmunimji.network

import com.project.smartmunimji.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Get token from TokenManager
        val token = tokenManager.getJwtToken()

        // If token exists, add it to the Authorization header
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}