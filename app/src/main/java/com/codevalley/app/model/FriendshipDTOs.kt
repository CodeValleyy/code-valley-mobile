package com.codevalley.app.model

import java.util.Date

data class FriendshipDTO(
    val senderId: Int,
    val receiverId: Int,
    val status: String, // "pending" | "accepted" | "declined"
    val createdAt: Date
)

data class FriendshipResponseDTO(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val status: String, // "pending" | "accepted" | "declined"
    val createdAt: Date
)
