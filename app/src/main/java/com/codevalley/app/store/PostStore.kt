package com.codevalley.app.store

import com.codevalley.app.model.PostResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PostStore {
    private val _posts = MutableStateFlow<List<PostResponseDto>>(emptyList())
    val posts: StateFlow<List<PostResponseDto>> = _posts

    fun setPosts(posts: List<PostResponseDto>) {
        _posts.value = posts
    }

    fun getPostById(postId: Int): PostResponseDto? {
        return _posts.value.firstOrNull { it.id == postId }
    }

    fun getPostsByUserId(userId: Int): List<PostResponseDto> {
        return _posts.value.filter { it.userId == userId }
    }

    fun getNumberOfPosts(): Int {
        return _posts.value.size
    }

    fun getNumberOfPostsByUserId(userId: Int): Int {
        return _posts.value.count { it.userId == userId }
    }

    fun updatePost(postId: Int, update: (PostResponseDto) -> PostResponseDto) {
        _posts.value = _posts.value.map {
            if (it.id == postId) {
                update(it)
            } else {
                it
            }
        }
    }
}
