package com.codevalley.app.model

data class GroupDTO(
    val name: String,
    val memberIds: List<Int>
)

data class GroupResponseDTO(
    val id: Int,
    val name: String,
    val members: List<UserResponseDTO>
)
