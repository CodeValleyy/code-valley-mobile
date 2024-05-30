package com.codevalley.app.repository

import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val retrofit: Retrofit
) {

    private fun createAuthorizedApiService(token: String): ApiService {
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

        return client.create(ApiService::class.java)
    }

    suspend fun getProfile(id: Int, token: String): UserResponseDTO {
        val authorizedApiService = createAuthorizedApiService(token)
        return authorizedApiService.getProfile(id)
    }
}
