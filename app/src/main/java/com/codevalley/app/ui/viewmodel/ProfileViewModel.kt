package com.codevalley.app.ui.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var profile by mutableStateOf<UserResponseDTO?>(null)
    var currentUser by mutableStateOf<UserResponseDTO?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadProfile(id: Int, token: String) {
        viewModelScope.launch {
            try {
                profile = userRepository.getProfile(id, token)
                currentUser = userRepository.getMe(token)
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Erreur lors du chargement du profil. Veuillez réessayer."
            }
        }
    }

    fun uploadAvatar(userId: Int, fileUri: Uri, token: String) {
        viewModelScope.launch {
            try {
                val avatarUrl = userRepository.uploadAvatar(userId, fileUri, token)
                profile?.avatar = avatarUrl
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Erreur lors du téléchargement de l'avatar. Veuillez réessayer."
            }
        }
    }
}
