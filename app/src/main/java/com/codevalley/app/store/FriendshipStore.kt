package com.codevalley.app.store

import com.codevalley.app.model.FollowersAndFollowingsCount
import com.codevalley.app.model.UserItemDTO
import kotlinx.coroutines.flow.MutableStateFlow

object FriendshipStore {
    private val _friends = MutableStateFlow<List<UserItemDTO.UserFriend>>(emptyList())
    val friends = _friends

    private val _pendingRequests = MutableStateFlow<List<UserItemDTO.UserFriend>>(emptyList())
    val pendingRequests = _pendingRequests

    private val _sentRequests = MutableStateFlow<List<UserItemDTO.UserFriend>>(emptyList())
    val sentRequests = _sentRequests

    private val _followersAndFollowingsCount = MutableStateFlow<FollowersAndFollowingsCount?>(null)
    val followersAndFollowingsCount = _followersAndFollowingsCount

    private val _followers = MutableStateFlow<List<UserItemDTO.UserFriend>>(emptyList())
    val followers = _followers

    private val _following = MutableStateFlow<List<UserItemDTO.UserFriend>>(emptyList())
    val following = _following


    /* GET */

    /* SET */
    fun setPendingRequests(pendingRequests: List<UserItemDTO.UserFriend>) {
        _pendingRequests.value = pendingRequests
    }

    fun setSentRequests(sentRequests: List<UserItemDTO.UserFriend>) {
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

    fun updatePendingRequest(pendingRequestId: Int, update: (UserItemDTO.UserFriend) -> UserItemDTO.UserFriend) {
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

    fun setFollowersAndFollowingsCount(response: FollowersAndFollowingsCount) {
        _followersAndFollowingsCount.value = response
    }

    fun setFollowers(response: List<UserItemDTO.UserFriend>) {
        _followers.value = response
    }

    fun setFollowing(response: List<UserItemDTO.UserFriend>) {
        _following.value = response
    }

}
