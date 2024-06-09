package com.codevalley.app.ui.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.repository.FriendshipRepository
import com.codevalley.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val friendshipRepository: FriendshipRepository,
) : ViewModel() {

    var profile by mutableStateOf<UserResponseDTO?>(null)
    var currentUser by mutableStateOf<UserResponseDTO?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(true)
    var isFollowing by mutableStateOf(false)

    fun loadProfile(id: Int) {
        viewModelScope.launch {
            try {
                profile = userRepository.getProfile(id)
                currentUser = userRepository.getMe()
                isFollowing = friendshipRepository.isFollowing(currentUser!!.id, id)
                isLoading = false
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Erreur lors du chargement du profil. Veuillez réessayer."
                isLoading = false
            }
        }
    }


    fun followUser(userId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.sendFriendRequest(userId)
                isFollowing = true
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Erreur lors du suivi de l'utilisateur. Veuillez réessayer."
            }
        }
    }

    fun unfollowUser(userId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriend(userId)
                isFollowing = false
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Erreur lors de l'annulation du suivi de l'utilisateur. Veuillez réessayer."
            }
        }
    }

    fun uploadAvatar(userId: Int, fileUri: Uri) {
        viewModelScope.launch {
            try {
                val avatarUrl = userRepository.uploadAvatar(userId, fileUri)
                profile?.avatar = avatarUrl
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Erreur lors du téléchargement de l'avatar. Veuillez réessayer."
            }
        }
    }
}
