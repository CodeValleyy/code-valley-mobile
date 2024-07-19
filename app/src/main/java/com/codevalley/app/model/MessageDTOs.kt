package com.codevalley.app.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class MessageDTO(
    @SerializedName("value")
    val value: String,

    @SerializedName("authorId")
    val authorId: String,

    @SerializedName("groupId")
    val groupId: String
)

data class MessageResponseDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("value")
    val value: String,

    @SerializedName("author")
    val author: UserResponseDTO,

    @SerializedName("group")
    val group: GroupResponseDTO,

    @SerializedName("createdAt")
    val createdAt: Date
)
