package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.UserItemDTO
import com.codevalley.app.repository.FriendshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val friendshipRepository: FriendshipRepository
) : ViewModel() {
    private val _followers = MutableStateFlow<List<UserItemDTO.UserFriend>>(emptyList())
    val followers: StateFlow<List<UserItemDTO.UserFriend>> = _followers

    private val _pendingRequests = MutableStateFlow<List<UserItemDTO.FriendshipPending>>(emptyList())
    val pendingRequests: StateFlow<List<UserItemDTO.FriendshipPending>> = _pendingRequests

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    internal fun loadFollowers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val friends = friendshipRepository.listFriends()
                val pending = friendshipRepository.listPendingRequests()
                _followers.value = friends
                _pendingRequests.value = pending
                _isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to load followers."
                _isLoading.value = false
            }
        }
    }

    internal fun loadFollowers(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val friends = friendshipRepository.listFriendsById(userId)
                val pending = friendshipRepository.listPendingRequests()
                _followers.value = friends
                _pendingRequests.value = pending
                _isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to load followers."
                _isLoading.value = false
            }
        }
    }

    fun acceptRequest(friendshipId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.acceptFriendRequest(friendshipId)
                loadFollowers()
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to accept friend request."
            }
        }
    }

    fun declineRequest(friendshipId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.declineFriendRequest(friendshipId)
                loadFollowers()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to decline friend request."
            }
        }
    }

    fun removeFriend(friendId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriend(friendId)
                loadFollowers()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to remove friend."
            }
        }
    }
}
