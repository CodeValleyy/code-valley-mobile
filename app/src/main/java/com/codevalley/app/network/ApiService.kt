package com.codevalley.app.network

import com.codevalley.app.model.UserResponseDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET("/auth/profile/{id}")
    suspend fun getProfile(@Path("id") id: Int): UserResponseDTO
}