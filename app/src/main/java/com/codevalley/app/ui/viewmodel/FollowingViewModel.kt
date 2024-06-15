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

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val friendshipRepository: FriendshipRepository
) : ViewModel() {
    private val _following = MutableStateFlow<List<UserItemDTO.UserFriend>>(emptyList())
    val following: StateFlow<List<UserItemDTO.UserFriend>> = _following

    private val _sentRequests = MutableStateFlow<List<UserItemDTO.FriendshipSent>>(emptyList())
    val sentRequests: StateFlow<List<UserItemDTO.FriendshipSent>> = _sentRequests

    private val _numberOfFollowing = MutableStateFlow(0)
    val numberOfFollowing: StateFlow<Int> = _numberOfFollowing

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage


    fun loadFollowing(userId: Int, limit: Int = 10, offset: Int = 0) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                friendshipRepository.listFollowing(userId, limit, offset)
                _following.value = FriendshipStore.following.value
                _isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to load following."
                _isLoading.value = false
            }
        }
    }

    fun followUser(userId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.sendFriendRequest(userId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to follow user."
            }
        }
    }

    fun unfollowUser(userId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriend(userId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to unfollow user."
            }
        }
    }
}
