package com.codevalley.app.model

import java.util.Date

data class FriendshipDTO(
    val senderId: Int,
    val receiverId: Int,
    val status: FriendshipStatus,
    val createdAt: Date
)

data class FriendshipResponseDTO(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val status: FriendshipStatus,
    val createdAt: Date
) {
    fun map(function: () -> UserItemDTO.FriendshipSent): List<UserItemDTO.FriendshipSent> {
        return listOf(function())
    }
}

data class RawFriendshipResponseDTO(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val status: String,
    val createdAt: Date
) {
    fun toFriendshipResponseDTO(): FriendshipResponseDTO {
        val status = when (this.status) {
            "pending" -> FriendshipStatus.PENDING
            "accepted" -> FriendshipStatus.ACCEPTED
            "declined" -> FriendshipStatus.DECLINED
            else -> throw IllegalArgumentException("Unknown status: ${this.status}")
        }
        return FriendshipResponseDTO(
            id = this.id,
            senderId = this.senderId,
            receiverId = this.receiverId,
            status = status,
            createdAt = this.createdAt
        )
    }

}

enum class FriendshipStatus {
    PENDING,
    ACCEPTED,
    DECLINED
}
