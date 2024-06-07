package com.codevalley.app.ui.viewmodel;

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.codevalley.app.model.ApiAuthResponse
import com.codevalley.app.repository.UserRepository
import com.codevalley.app.ui.navigation.ScreenName
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
    var isWaiting by mutableStateOf(false)

    fun register(navController: NavController) {
        if (username == "") {
            errorMessage = "Please enter an username"
        }
        else if (email == "") {
            errorMessage = "Please enter an email"
        }
        else if (password == "") {
            errorMessage = "Please enter your password"
        }
        else if (samePassword == "") {
            errorMessage = "Please confirm your password"
        }
        else if (password != samePassword) {
            errorMessage = "Passwords does not match"
        }
        else {
            errorMessage = ""
            isWaiting = true

            viewModelScope.launch {
                when (val registerResponse = userRepository.register(username, email, password)) {
                    is ApiAuthResponse.Success -> {
                        username = ""
                        email = ""
                        password = ""
                        samePassword = ""
                        errorMessage = ""
                        isWaiting = false

                        when (val loginResponse = userRepository.login(email, password)) {
                            is ApiAuthResponse.Success -> {
                                TokenManager.token = loginResponse.data.accessToken
                                navController.navigate(ScreenName.Profile.toString())
                            }
                            is ApiAuthResponse.Error -> {
                                TokenManager.token = null
                                navController.navigate(ScreenName.Login.toString())
                            }
                        }
                    }
                    is ApiAuthResponse.Error -> {
                        val error = registerResponse.error
                        isWaiting = false
                        errorMessage = error.getMessage()
                        TokenManager.token = null
                    }
                }
            }
        }
    }
}
