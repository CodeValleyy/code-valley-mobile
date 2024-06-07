package com.codevalley.app.model

import java.util.Date

data class CreatePostDto(
    val content: String
)

data class LikePostDto(
    val postId: Int
)

data class LikePostResponseDto(
    val id: Int,
    val likes: Int
)

data class PostResponseDto(
    val id: Int,
    val content: String,
    val userId: Int,
    val username: String,
    val createdAt: Date,
    val avatar: String,
    val likes: Int,
    val userHasLiked: Boolean
)

data class Comment(
    val id: Int,
    val avatar: String,
    val username: String,
    val userId: Int,
    val content: String,
    val createdAt: Date,
    val hasLiked: Boolean
)
