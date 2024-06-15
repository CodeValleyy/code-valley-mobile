package com.codevalley.app.network

import com.codevalley.app.model.CreatePostDto
import com.codevalley.app.model.LikePostResponseDto
import com.codevalley.app.model.PostResponseDto
import com.codevalley.app.model.RawPostResponseDto
import retrofit2.http.*

interface PostService {
    @GET("/posts")
    suspend fun getPosts(): List<RawPostResponseDto>

    @POST("/posts")
    suspend fun createPost(@Body createPostDto: CreatePostDto): RawPostResponseDto

    @DELETE("/posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)

    @POST("/posts/{id}/like")
    suspend fun likePost(@Path("id") id: Int): LikePostResponseDto

    @DELETE("/posts/{id}/like")
    suspend fun unlikePost(@Path("id") id: Int): LikePostResponseDto
}
