package com.codevalley.app.repository

import com.codevalley.app.model.CommentResponseDto
import com.codevalley.app.model.CreateCommentDto
import com.codevalley.app.network.PostCommentService
import com.codevalley.app.network.createAuthorizedApiService
import retrofit2.Retrofit
import javax.inject.Inject

class PostCommentRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    private fun createAuthorizedApiService(): PostCommentService {
        return createAuthorizedApiService(retrofit, PostCommentService::class.java)
    }

    suspend fun createComment(postId: Int, createCommentDto: CreateCommentDto): CommentResponseDto {
        val commentService = createAuthorizedApiService()
        return commentService.createComment(postId, createCommentDto)
    }

    suspend fun getComments(postId: Int, limit: Int = 10, offset: Int = 0): List<CommentResponseDto> {
        val commentService = createAuthorizedApiService()
        return commentService.getComments(postId, limit, offset)
    }

    suspend fun deleteComment(postId: Int, commentId: Int) {
        val commentService = createAuthorizedApiService()
        commentService.deleteComment(postId, commentId)
    }
}