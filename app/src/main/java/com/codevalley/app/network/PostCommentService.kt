package com.codevalley.app.network

import com.codevalley.app.model.CommentResponseDto
import com.codevalley.app.model.CreateCommentDto
import com.codevalley.app.model.LikePostResponseDto
import com.codevalley.app.model.RawPostResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PostCommentService {
    @POST("/posts/{postId}/comments")
    suspend fun createComment(
        @Path("postId") postId: Int,
        @Body createCommentDto: CreateCommentDto
    ): CommentResponseDto

    @GET("/posts/{postId}/comments")
    suspend fun getComments(
        @Path("postId") postId: Int,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): List<CommentResponseDto>

    @DELETE("/posts/{postId}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("postId") postId: Int,
        @Path("commentId") commentId: Int
    )
}
