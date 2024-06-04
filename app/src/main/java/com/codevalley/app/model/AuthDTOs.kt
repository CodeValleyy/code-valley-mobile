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
