package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.PostResponseDto
import com.codevalley.app.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _posts = MutableStateFlow<List<PostResponseDto>>(emptyList())
    val posts: StateFlow<List<PostResponseDto>> = _posts

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun loadPosts(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val posts = postRepository.getPosts(token)
                _posts.value = posts
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load posts."
            }
        }
    }

    fun likePost(token: String, postId: Int) {
        viewModelScope.launch {
            try {
                postRepository.likePost(token, postId)
                loadPosts(token)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to like post."
            }
        }
    }

    fun unlikePost(token: String, postId: Int) {
        viewModelScope.launch {
            try {
                postRepository.unlikePost(token, postId)
                loadPosts(token)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to unlike post."
            }
        }
    }
}
