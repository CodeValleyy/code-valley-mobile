package com.codevalley.app.repository

import android.content.Context
import com.codevalley.app.model.CreatePostDto
import com.codevalley.app.model.LikePostResponseDto
import com.codevalley.app.model.PostResponseDto
import com.codevalley.app.network.PostService
import com.codevalley.app.network.createAuthorizedApiService
import retrofit2.Retrofit
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val retrofit: Retrofit,
    private val context: Context
) {

    private fun createAuthorizedApiService(token: String): PostService {
        return createAuthorizedApiService(token, retrofit, PostService::class.java)
    }

    suspend fun getPosts(token: String): List<PostResponseDto> {
        val authorizedApiService = createAuthorizedApiService(token)
        return authorizedApiService.getPosts()
    }

    suspend fun createPost(token: String, createPostDto: CreatePostDto): PostResponseDto {
        val authorizedApiService = createAuthorizedApiService(token)
        return authorizedApiService.createPost(createPostDto)
    }

    suspend fun deletePost(token: String, id: Int) {
        val authorizedApiService = createAuthorizedApiService(token)
        authorizedApiService.deletePost(id)
    }

    suspend fun likePost(token: String, id: Int): LikePostResponseDto {
        val authorizedApiService = createAuthorizedApiService(token)
        return authorizedApiService.likePost(id)
    }

    suspend fun unlikePost(token: String, id: Int): LikePostResponseDto {
        val authorizedApiService = createAuthorizedApiService(token)
        return authorizedApiService.unlikePost(id)
    }
}
