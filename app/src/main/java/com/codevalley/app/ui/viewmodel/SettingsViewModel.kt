package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.codevalley.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isTwoFactorEnabled = MutableStateFlow(false)
    val isTwoFactorEnabled: StateFlow<Boolean> = _isTwoFactorEnabled

    private val _qrCodeUrl = MutableStateFlow("")
    val qrCodeUrl: StateFlow<String> = _qrCodeUrl

    private val _setupKey = MutableStateFlow("")
    val setupKey: StateFlow<String> = _setupKey

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun loadTwoFactorStatus() {
        viewModelScope.launch {
            try {
                val user = userRepository.getMe()
                _isTwoFactorEnabled.value = user.isTwoFactorAuthenticationEnabled
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load two-factor status."
            }
        }
    }

    fun enableTwoFactor() {
        viewModelScope.launch {
            try {
                userRepository.turnOnTwoFactorAuthentication()
                val (qrCodeUrl, setupKey) = userRepository.generateTwoFactor()
                _qrCodeUrl.value = qrCodeUrl
                _setupKey.value = setupKey
                println("QR code URL: $qrCodeUrl")
                _isTwoFactorEnabled.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Failed to enable two-factor authentication."
            }
        }
    }

    fun disableTwoFactor() {
        viewModelScope.launch {
            try {
                userRepository.turnOffTwoFactorAuthentication()
                _isTwoFactorEnabled.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to disable two-factor authentication."
            }
        }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            try {
                userRepository.logout()
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to logout."
            }
        }
    }
}
