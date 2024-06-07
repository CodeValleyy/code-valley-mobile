package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.PostResponseDto
import com.codevalley.app.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _posts = MutableStateFlow<List<PostResponseDto>>(emptyList())
    val posts: StateFlow<List<PostResponseDto>> = _posts

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

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
}
