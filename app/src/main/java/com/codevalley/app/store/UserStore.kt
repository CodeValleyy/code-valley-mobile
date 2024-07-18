package com.codevalley.app.store

import com.codevalley.app.model.UserResponseDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserStore {
    private val _currentUser = MutableStateFlow<UserResponseDTO?>(null)
    var currentUser: StateFlow<UserResponseDTO?> = _currentUser

    fun getUserProfile(): UserResponseDTO? {
        return _currentUser.value
    }

    fun setUserProfile(user: UserResponseDTO) {
        _currentUser.value = user
    }

    fun clearUserProfile() {
        _currentUser.value = null
    }
}
