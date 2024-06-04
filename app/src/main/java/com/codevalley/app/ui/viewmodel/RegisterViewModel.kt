package com.codevalley.app.ui.viewmodel;

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.repository.UserRepository
import com.codevalley.app.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var samePassword by mutableStateOf("")
    var errorMessage by mutableStateOf("")

    fun register(): Boolean {
        if (username == "") {
            errorMessage = "Please enter an username"
            return false
        }
        if (email == "") {
            errorMessage = "Please enter an email"
            return false
        }
        if (password == "") {
            errorMessage = "Please enter your password"
            return false
        }
        if (samePassword == "") {
            errorMessage = "Please confirm your password"
            return false
        }
        if (password != samePassword) {
            errorMessage = "Passwords does not match"
            return false
        }
        else {
            var success = false
            viewModelScope.launch {
                try {
                    TokenManager.token = userRepository.register(username, email, password).accessToken
                    success = true
                } catch (e: Exception) {
                    errorMessage = "An error occured during the creation of your account"
                    TokenManager.token = null
                }
            }
            return success
        }
    }
}

