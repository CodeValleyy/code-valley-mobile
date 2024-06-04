package com.codevalley.app.model

data class TfCodeAuthDto(
    val  twoFactorAuthenticationCode: String,
)

data class TokenResponse(
    val accessToken: String,
)

data class UploadAvatarResponseDTO(
    val avatarUrl: String
)

data class LoginRequestDTO(
    val email: String,
    val password: String
)

data class RegisterRequestDTO(
    val username: String,
    val email: String,
    val password: String
)