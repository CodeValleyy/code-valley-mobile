package com.codevalley.app.model

import java.util.Date

data class NotificationDto(
    val id: Int,
    val createdAt: Date,
    val hasBeenRead: Boolean,
    val fromUsername: String,
    val fromUserId: Int,
    val linkId: Int,
    val notificationType: NotificationType
)

enum class NotificationType {
    friendshipReceived,
    friendshipAccepted,
    friendshipRefused,
    like,
    post
}

data class NotificationCountDto(
    val count: Int
)
