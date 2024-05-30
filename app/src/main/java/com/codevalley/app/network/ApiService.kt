package com.codevalley.app.network

import com.codevalley.app.model.UploadAvatarResponseDTO
import com.codevalley.app.model.UserResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("/auth/profile/{id}")
    suspend fun getProfile(@Path("id") id: Int): UserResponseDTO

    @Multipart
    @POST("/auth/avatar")
    suspend fun uploadAvatar(
        @Part("userId") userId: RequestBody,
        @Part file: MultipartBody.Part
    ): UploadAvatarResponseDTO
}