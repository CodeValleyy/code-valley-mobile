package com.codevalley.app.ui.viewmodel;

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
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
                try {
                    userRepository.register(username, email, password)
                    //TokenManager.token = userRepository.login(email, password).accessToken
                    username = ""
                    email = ""
                    password = ""
                    samePassword = ""
                    errorMessage = ""
                    isWaiting = false
                    navController.navigate(ScreenName.Profile.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    isWaiting = false
                    errorMessage = "An error occured during the creation of your account"
                    TokenManager.token = null
                }
            }
        }
    }
}
