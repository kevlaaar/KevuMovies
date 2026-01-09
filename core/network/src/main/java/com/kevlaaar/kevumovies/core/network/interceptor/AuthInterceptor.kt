package com.kevlaaar.kevumovies.core.network.interceptor

import com.kevlaaar.kevumovies.core.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${BuildConfig.TMDB_API_TOKEN}")
            .header("Accept","application/json")
            .build()

        return chain.proceed(newRequest)
    }
}