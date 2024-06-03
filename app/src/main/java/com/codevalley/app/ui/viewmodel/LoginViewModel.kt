package com.codevalley.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var token = ""
    var errorMessage by mutableStateOf("")

    fun login(email: String, password: String) {
        if (email == "") {
            errorMessage = "Please enter an email"
        }
        else if (password == "") {
            errorMessage = "Please enter your password"
        }
        else {
            viewModelScope.launch {
                try {
                    token = userRepository.login(email, password).accessToken
                } catch (e: Exception) {
                    errorMessage = "Email or password incorrect"
                }
            }
        }
    }
}
