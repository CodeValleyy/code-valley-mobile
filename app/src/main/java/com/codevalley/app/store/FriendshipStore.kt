package com.codevalley.app.store

import com.codevalley.app.model.UserItemDTO
import kotlinx.coroutines.flow.MutableStateFlow

object FriendshipStore {
   private val _friends = MutableStateFlow<List<UserItemDTO.UserFriend>>(emptyList())
    val friends = _friends

    private val _pendingRequests = MutableStateFlow<List<UserItemDTO.FriendshipPending>>(emptyList())
    val pendingRequests = _pendingRequests

    private val _sentRequests = MutableStateFlow<List<UserItemDTO.FriendshipSent>>(emptyList())
    val sentRequests = _sentRequests


    /* GET */
    fun getPendingRequestById(pendingRequestId: Int): UserItemDTO.FriendshipPending? {
        return _pendingRequests.value.firstOrNull { it.id == pendingRequestId }
    }

    fun getNumberOfPendingRequests(): Int {
        return _pendingRequests.value.size
    }

    fun getNumberOfSentRequests(): Int {
        return _sentRequests.value.size
    }

    fun getFriendById(friendId: Int): UserItemDTO.UserFriend? {
        return _friends.value.firstOrNull { it.id == friendId }
    }

    fun getNumberOfFriends(): Int {
        return _friends.value.size
    }

    fun getFriends(): List<UserItemDTO.UserFriend> {
        return _friends.value
    }

    /* SET */
    fun setPendingRequests(pendingRequests: List<UserItemDTO.FriendshipPending>) {
        _pendingRequests.value = pendingRequests
    }

    fun setSentRequests(sentRequests: List<UserItemDTO.FriendshipSent>) {
        _sentRequests.value = sentRequests
    }

    fun setFriends(friends: List<UserItemDTO.UserFriend>) {
        _friends.value = friends
    }

    /* UPDATE */
    fun updateFriend(friendId: Int, update: (UserItemDTO.UserFriend) -> UserItemDTO.UserFriend) {
        _friends.value = _friends.value.map {
            if (it.id == friendId) {
                update(it)
            } else {
                it
            }
        }
    }

    fun updatePendingRequest(pendingRequestId: Int, update: (UserItemDTO.FriendshipPending) -> UserItemDTO.FriendshipPending) {
        _pendingRequests.value = _pendingRequests.value.map {
            if (it.id == pendingRequestId) {
                update(it)
            } else {
                it
            }
        }
    }

    fun getNumberOfFollowers(): Int {
        return _friends.value.size

    }

    fun getNumberOfFollowing(): Int {
        return _friends.value.size
    }

    fun reset() {
        _friends.value = emptyList()
        _pendingRequests.value = emptyList()
        _sentRequests.value = emptyList()
    }

}
