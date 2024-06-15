package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.UserItemDTO
import com.codevalley.app.repository.FriendshipRepository
import com.codevalley.app.store.FriendshipStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.codevalley.app.model.FriendshipStatus

@HiltViewModel
class FollowersViewModel @Inject constructor(
    private val friendshipRepository: FriendshipRepository
) : ViewModel() {
    private val _followers = MutableStateFlow<List<UserItemDTO.UserFriend>>(emptyList())
    val followers: StateFlow<List<UserItemDTO.UserFriend>> = _followers

    private val _pendingRequests = MutableStateFlow<List<UserItemDTO.FriendshipPending>>(emptyList())
    val pendingRequests: StateFlow<List<UserItemDTO.FriendshipPending>> = _pendingRequests

    private val _numberOfFollowers = MutableStateFlow(0)
    val numberOfFollowers: StateFlow<Int> = _numberOfFollowers

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun loadFollowers(userId: Int, limit: Int, offset: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                friendshipRepository.listFollowers(userId, limit, offset)
                _followers.value = FriendshipStore.followers.value
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load followers."
                _isLoading.value = false
            }
        }
    }

    fun acceptRequest(friendId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.acceptFriendRequest(friendId)
                _followers.value = _followers.value.map { if (it.id == friendId) it.copy(status = FriendshipStatus.ACCEPTED) else it }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to accept friend request."
            }
        }
    }

    fun declineRequest(friendId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.declineFriendRequest(friendId)
                _followers.value = _followers.value.filter { it.id != friendId }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to decline friend request."
            }
        }
    }

    fun removeFriend(friendId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriend(friendId)
                _followers.value = _followers.value.filter { it.id != friendId }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to remove friend."
            }
        }
    }
}
