package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.FriendshipStatus
import com.codevalley.app.model.PostResponseDto
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.repository.FriendshipRepository
import com.codevalley.app.repository.PostRepository
import com.codevalley.app.utils.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val friendshipRepository: FriendshipRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _posts = MutableStateFlow<List<PostResponseDto>>(emptyList())
    val posts: StateFlow<List<PostResponseDto>> = _posts

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    val userProfile: UserResponseDTO?
        get() = UserStore.userProfile

    fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val posts = postRepository.getPosts()
                _posts.value = posts
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
                null
            } else {
                throw e
            }
        }
    }
}