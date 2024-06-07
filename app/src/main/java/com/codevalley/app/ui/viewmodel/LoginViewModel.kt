package com.codevalley.app.ui.viewmodel

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
import com.codevalley.app.utils.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf("")
    var isWaiting by mutableStateOf(false)

    fun initialize(navController: NavController) {
        viewModelScope.launch {
            checkIfUserIsLoggedIn(navController)
        }
    }


    private suspend fun checkIfUserIsLoggedIn(navController: NavController) {
        val token = TokenManager.token
        if (token != null) {
            try {
                val profile = userRepository.getMe()
                UserStore.userProfile = profile
                navController.navigate(ScreenName.NewsFeed.toString())
            } catch (e: Exception) {
                TokenManager.clearToken()
            }
        }
    }

    fun login(navController: NavController) {
        if (email == "") {
            errorMessage = "Please enter an email"
        }
        else if (password == "") {
            errorMessage = "Please enter your password"
        }
        else {
            errorMessage = ""
            isWaiting = true
            viewModelScope.launch {
                when (val apiResponse = userRepository.login(email, password)) {
                    is ApiAuthResponse.Success -> {
                        TokenManager.token = apiResponse.data.accessToken
                        val profile = userRepository.getMe()
                        UserStore.userProfile = profile
                        email = ""
                        password = ""
                        errorMessage = ""
                        isWaiting = false
                        navController.navigate(ScreenName.NewsFeed.toString())
                    }
                    is ApiAuthResponse.Error -> {
                        val error = apiResponse.error
                        isWaiting = false
                        errorMessage = error.getMessage()
                        TokenManager.clearToken()
                    }
                }
            }
        }
    }
}
