package com.codevalley.app.ui.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.repository.FriendshipRepository
import com.codevalley.app.repository.NotificationRepository
import com.codevalley.app.repository.UserRepository
import com.codevalley.app.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val friendshipRepository: FriendshipRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    var profile by mutableStateOf<UserResponseDTO?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(true)
    var countNotification by mutableIntStateOf(0)

    fun loadProfile(id: Int) {
        viewModelScope.launch {
            try {
                profile = userRepository.getProfile(id)
                isLoading = false
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Erreur lors du chargement du profil. Veuillez réessayer."
                isLoading = false
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

    fun countFollowersAndFollowing(userId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.fetchFollowersAndFollowingsCount(userId)
            } catch (e: Exception) {
                errorMessage = "Failed to count followers and following."
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                userRepository.logout()
                UserStore.clearUserProfile()
            } catch (e: Exception) {
                errorMessage = "Failed to logout."
            }
        }
    }

    fun countUnreadNotifications() {
        viewModelScope.launch {
            try {
                countNotification = notificationRepository.getNotificationCount().count
            } catch (e: Exception) {
                errorMessage = "Failed to count unread notifications."
            }
        }
    }
}
