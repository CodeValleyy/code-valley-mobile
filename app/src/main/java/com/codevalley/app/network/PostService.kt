package com.codevalley.app.network

import com.codevalley.app.model.LikePostResponseDto
import com.codevalley.app.model.RawPostResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PostService {
    @GET("/posts")
    suspend fun getPosts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<RawPostResponseDto>

    @Multipart
    @POST("/posts")
    suspend fun createPost(
        @Part("content") content: RequestBody,
        @Part file: MultipartBody.Part?
    ): RawPostResponseDto
    @DELETE("/posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)

    @POST("/posts/{id}/like")
    suspend fun likePost(@Path("id") id: Int): LikePostResponseDto

    @DELETE("/posts/{id}/like")
    suspend fun unlikePost(@Path("id") id: Int): LikePostResponseDto
}
