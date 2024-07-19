package com.codevalley.app.model
import com.google.gson.annotations.SerializedName

data class GroupDTO(
    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("isPublic")
    val isPublic: Boolean = false,
)

data class MultipartFile(
    val fileName: String,
    val fileType: String,
    val fileContent: ByteArray
)

data class GroupResponseDTO(
    val id: Int,
    val name: String,
    val description: String? = null,
    val avatar: String? = null,
    val members: List<UserResponseDTO>,
    val admins: List<UserResponseDTO>,
    val isPublic: Boolean,
    val memberJoinRequests: List<UserResponseDTO>
)