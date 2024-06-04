package com.codevalley.app.network

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

fun <T> createAuthorizedApiService(token: String, retrofit: Retrofit, service: Class<T>): T {
    val authInterceptor = okhttp3.Interceptor { chain ->
        val original = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()
            .header("Authorization", "Bearer $token")
        val request: Request = requestBuilder.build()
        chain.proceed(request)
    }

    val client: Retrofit = retrofit.newBuilder()
        .client(
            OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
        )
        .build()

    return client.create(service)
}
