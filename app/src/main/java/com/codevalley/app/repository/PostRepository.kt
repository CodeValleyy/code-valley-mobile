package com.codevalley.app.repository

import android.content.Context
import com.codevalley.app.model.CreatePostDto
import com.codevalley.app.model.LikePostResponseDto
import com.codevalley.app.model.PostResponseDto
import com.codevalley.app.model.RawPostResponseDto
import com.codevalley.app.network.PostService
import com.codevalley.app.network.createAuthorizedApiService
import com.codevalley.app.utils.PostUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Retrofit
import javax.inject.Inject
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class PostRepository @Inject constructor(
    private val retrofit: Retrofit,
    private val context: Context
) {

    private val client = OkHttpClient()

    private val postService: PostService by lazy {
        createAuthorizedApiService(retrofit, PostService::class.java)
    }

    suspend fun fetchPosts(limit: Int, offset: Int): List<PostResponseDto> {
        try {
            val posts = withContext(Dispatchers.IO) {
                postService.getPosts(limit, offset)
            }

            val postsWithCode: List<PostResponseDto> = posts.map { post ->
                if (post.code_url != null) {
                    val code = getCode(post.code_url)
                    val code_language = PostUtils.getCodeLanguageFromUrl(post.code_url)

                    PostResponseDto(
                        id = post.id,
                        content = post.content,
                        userId = post.userId,
                        username = post.username,
                        createdAt = post.createdAt,
                        avatar = post.avatar,
                        likes = post.likes,
                        userHasLiked = post.userHasLiked,
                        fileId = post.fileId,
                        code_url = post.code_url,
                        code = code,
                        code_language = code_language
                    )
                } else {
                    PostResponseDto(
                        id = post.id,
                        content = post.content,
                        userId = post.userId,
                        username = post.username,
                        createdAt = post.createdAt,
                        avatar = post.avatar,
                        likes = post.likes,
                        userHasLiked = post.userHasLiked,
                        fileId = post.fileId,
                        code_url = post.code_url,
                        code = null,
                        code_language = null
                    )
                }
            }

            return postsWithCode.sortedByDescending { it.createdAt }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e // Relancer l'exception après logging
        }
    }

    suspend fun createPost(content: RequestBody, file: MultipartBody.Part?) {
        postService.createPost(content, file)
    }

    suspend fun deletePost(id: Int) {
        postService.deletePost(id)
    }

    suspend fun likePost(id: Int): LikePostResponseDto {
        return postService.likePost(id)
    }

    suspend fun unlikePost(id: Int): LikePostResponseDto {
        return postService.unlikePost(id)
    }

    suspend fun getCode(url: String): String {
        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string() ?: throw IOException("Empty response body")
            } catch (e: IOException) {
                e.printStackTrace()
                throw e
            }
        }
    }
}
