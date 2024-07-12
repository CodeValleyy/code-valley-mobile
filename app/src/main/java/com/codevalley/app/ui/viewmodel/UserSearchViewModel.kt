package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.network.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserResponseDTO>>(emptyList())
    val users: StateFlow<List<UserResponseDTO>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun searchUsers(username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authService.searchUsers(username)
                _users.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to search users."
            } finally {
                _isLoading.value = false
            }
        }
    }
}