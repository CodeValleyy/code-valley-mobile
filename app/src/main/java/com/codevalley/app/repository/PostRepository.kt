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

    private fun createAuthorizedApiService(): PostService {
        return createAuthorizedApiService(retrofit, PostService::class.java)
    }

    suspend fun getPosts(): List<PostResponseDto> {
        val authorizedApiService = createAuthorizedApiService()
        return authorizedApiService.getPosts()
    }

    suspend fun createPost(createPostDto: CreatePostDto): PostResponseDto {
        val authorizedApiService = createAuthorizedApiService()
        return authorizedApiService.createPost(createPostDto)
    }

    suspend fun deletePost(id: Int) {
        val authorizedApiService = createAuthorizedApiService()
        authorizedApiService.deletePost(id)
    }

    suspend fun likePost(id: Int): LikePostResponseDto {
        val authorizedApiService = createAuthorizedApiService()
        return authorizedApiService.likePost(id)
    }

    suspend fun unlikePost(id: Int): LikePostResponseDto {
        val authorizedApiService = createAuthorizedApiService()
        return authorizedApiService.unlikePost(id)
    }
}
