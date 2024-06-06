package com.codevalley.app.model

data class TfCodeAuthDto(
    val twoFactorAuthenticationCode: String,
)

data class TokenResponse(
    val accessToken: String,
)

data class ApiError(
    val message: Any,
    val error: String,
    val statusCode: Int
) {
    fun getMessage(): String {
        return when (message) {
            is String -> message
            is List<*> -> message.filterIsInstance<String>().firstOrNull() ?: "Unknown error"
            else -> "Unknown error"
        }
    }
}

sealed class ApiAuthResponse<out T> {
    data class Success<out T>(val data: T) : ApiAuthResponse<T>()
    data class Error(val error: ApiError) : ApiAuthResponse<Nothing>()
}

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
