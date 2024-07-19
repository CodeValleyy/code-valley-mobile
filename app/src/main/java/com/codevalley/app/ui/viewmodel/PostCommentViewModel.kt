package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.CommentResponseDto
import com.codevalley.app.model.CreateCommentDto
import com.codevalley.app.repository.PostCommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostCommentViewModel @Inject constructor(
    private val commentRepository: PostCommentRepository
) : ViewModel() {

    private val _comments = MutableStateFlow<List<CommentResponseDto>>(emptyList())
    val comments: StateFlow<List<CommentResponseDto>> = _comments.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadComments(postId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val comments = commentRepository.getComments(postId)
                _comments.value = comments
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addComment(postId: Int, content: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val newComment = commentRepository.createComment(postId, CreateCommentDto(content))
                _comments.value = _comments.value + newComment
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteComment(postId: Int, commentId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                commentRepository.deleteComment(postId, commentId)
                _comments.value = _comments.value.filter { it.id != commentId }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
