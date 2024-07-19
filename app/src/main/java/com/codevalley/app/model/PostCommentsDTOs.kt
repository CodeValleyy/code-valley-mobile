package com.codevalley.app.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class CommentResponseDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("content")
    val content: String,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("avatar")
    val avatar: String,

    @SerializedName("postId")
    val postId: Int,

    @SerializedName("createdAt")
    val createdAt: Date
)

data class CreateCommentDto(
    @SerializedName("content")
    val content: String
)
