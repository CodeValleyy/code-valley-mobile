package com.codevalley.app.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.CreatePostDto
import com.codevalley.app.model.FriendshipStatus
import com.codevalley.app.model.PostResponseDto
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.repository.FriendshipRepository
import com.codevalley.app.repository.PostRepository
import com.codevalley.app.store.PostStore
import com.codevalley.app.store.UserStore
import com.codevalley.app.utils.PostUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val friendshipRepository: FriendshipRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    val posts: StateFlow<List<PostResponseDto>> = PostStore.posts

    val userProfile: UserResponseDTO?
        get() = UserStore.userProfile

    fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val posts = postRepository.fetchPosts()
                PostStore.setPosts(posts)
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load posts."
            }
        }
    }

    fun likePost(postId: Int) {
        viewModelScope.launch {
            try {
                postRepository.likePost(postId)
                loadPosts()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to like post."
            }
        }
    }

    fun unlikePost(postId: Int) {
        viewModelScope.launch {
            try {
                postRepository.unlikePost(postId)
                loadPosts()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to unlike post."
            }
        }
    }

    fun sendFriendRequest(receiverId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.sendFriendRequest(receiverId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to send friend request."
            }
        }
    }

    fun removeFriend(friendId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriend(friendId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to remove friend."
            }
        }
    }

    suspend fun getFriendshipStatus(userId: Int): FriendshipStatus? {
        return try {
            val status = friendshipRepository.getFriendshipStatus(userId)
            status.status
        } catch (e: HttpException) {
            if (e.code() == 404) {
                null // Not friends
            } else {
                throw e
            }
        }
    }

    fun createPost(context: Context, content: CreatePostDto, fileUri: Uri?) {
        viewModelScope.launch {
            try {
                val contentBody = content.content.toRequestBody("text/plain".toMediaTypeOrNull())

                val filePart = fileUri?.let { uri ->
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val file = File(context.cacheDir, uri.lastPathSegment ?: "tempFile")
                    val outputStream = FileOutputStream(file)
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }

                    val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("file", file.name, requestBody)
                }

                postRepository.createPost(contentBody, filePart)
                loadPosts()
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to create post."
            }
        }
    }

    fun deletePost(id: Int) {
        viewModelScope.launch {
            try {
                postRepository.deletePost(id)
                loadPosts()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete post."
            }
        }

    }

    private fun updatePostLocal(postId: Int, update: (PostResponseDto) -> PostResponseDto) {
        PostStore.updatePost(postId, update)
    }
}
