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
class FollowingViewModel @Inject constructor(
    private val friendshipRepository: FriendshipRepository
) : ViewModel() {
    private val _following = MutableStateFlow<List<UserItemDTO.UserFriend>>(emptyList())
    val following: StateFlow<List<UserItemDTO.UserFriend>> = _following

    private val _sentRequests = MutableStateFlow<List<UserItemDTO.FriendshipSent>>(emptyList())
    val sentRequests: StateFlow<List<UserItemDTO.FriendshipSent>> = _sentRequests

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun loadFollowing() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _following.value = friendshipRepository.listFriends()
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load following."
                _isLoading.value = false
            }
        }
    }

    fun loadSentRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _sentRequests.value = friendshipRepository.listSentRequests()
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load sent requests."
                _isLoading.value = false
            }
        }
    }

    fun cancelRequest(receiverId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.cancelFriendRequest(receiverId)
                loadSentRequests()
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Failed to cancel friend request."
            }
        }
    }

    fun removeFriend(friendId: Int) {
        viewModelScope.launch {
            try {
                friendshipRepository.removeFriend(friendId)
                loadFollowing()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to remove friend."
            }
        }
    }
}
