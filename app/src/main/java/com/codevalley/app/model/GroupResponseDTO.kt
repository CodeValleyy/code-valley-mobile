package com.codevalley.app.model

data class GroupResponseDTO(
    val id: Int,
    val name: String,
    val members: List<UserResponseDTO>
)
