package com.codevalley.app.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserFriendDTO(
    val id: Int,
    val email: String,
    val username: String
)

data class UserQueryDTO(
    val id: Int,
    val email: String,
    val username: String
)

data class UserResponseDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("lastLoginAt")
    val lastLoginAt: Date,

    @SerializedName("createdAt")
    val createdAt: Date,

    @SerializedName("avatar")
    var avatar: String,

    @SerializedName("twoFactorAuthenticationSecret")
    val twoFactorAuthenticationSecret: String,

    @SerializedName("isTwoFactorAuthenticationEnabled")
    val isTwoFactorAuthenticationEnabled: Boolean
)
