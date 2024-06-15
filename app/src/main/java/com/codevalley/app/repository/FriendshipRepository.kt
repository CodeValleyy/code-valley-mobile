package com.codevalley.app.repository

import com.codevalley.app.model.FriendshipResponseDTO
import com.codevalley.app.model.FriendshipStatus
import com.codevalley.app.model.UserItemDTO
import com.codevalley.app.model.UserQueryDTO
import com.codevalley.app.network.FriendshipService
import com.codevalley.app.network.createAuthorizedApiService
import com.codevalley.app.store.FriendshipStore
import retrofit2.Retrofit
import javax.inject.Inject

class FriendshipRepository @Inject constructor(
    private val retrofit: Retrofit
) {
    private val friendshipService: FriendshipService by lazy {
        createAuthorizedApiService(retrofit, FriendshipService::class.java)
    }

    suspend fun sendFriendRequest(receiverId: Int): FriendshipResponseDTO {
        return friendshipService.sendFriendRequest(receiverId)
    }

    suspend fun acceptFriendRequest(friendId: Int): FriendshipResponseDTO {
        return friendshipService.acceptFriendRequest(friendId)
    }

    suspend fun declineFriendRequest(friendId: Int) {
        friendshipService.declineFriendRequest(friendId)
    }

    suspend fun removeFriend(friendId: Int) {
        friendshipService.removeFriend(friendId)
    }

    suspend fun listPendingRequests(): List<UserItemDTO.FriendshipPending> {
        return friendshipService.listPendingRequests()
    }

    suspend fun cancelFriendRequest(receiverId: Int) {
        friendshipService.cancelFriendRequest(receiverId)
    }

    suspend fun listFriends(): List<UserItemDTO.UserFriend> {
        val response = friendshipService.listFriends()
        FriendshipStore.setFriends(response)
        return response
    }

    suspend fun listFriendsById(friendId: Int): List<UserItemDTO.UserFriend> {
        val response = friendshipService.listFriendsById(friendId)
        FriendshipStore.setFriends(response)
        return response
    }

    suspend fun getFriendshipStatus(friendId: Int): FriendshipResponseDTO {
        val response = friendshipService.getFriendshipStatus(friendId)
        return response.toFriendshipResponseDTO()
    }

    suspend fun listFriendSuggestions(): List<UserQueryDTO> {
        return friendshipService.listFriendSuggestions()
    }

    suspend fun isFollowing(currentUserId: Int, targetUserId: Int): Boolean {
        return friendshipService.isFollowing(currentUserId, targetUserId)
    }

    suspend fun listFollowers(userId: Int, limit: Int, offset: Int) {
        val response =friendshipService.listFollowers(userId, limit, offset)
        FriendshipStore.setFollowers(response)
        FriendshipStore.setPendingRequests(response.filter { it.status == FriendshipStatus.PENDING })
    }

    suspend fun listFollowing(userId: Int, limit: Int, offset: Int) {
        val response = friendshipService.listFollowing(userId, limit, offset)
        FriendshipStore.setFollowing(response)
        FriendshipStore.setSentRequests(response.filter { it.status == FriendshipStatus.PENDING })
    }

    suspend fun fetchFollowersAndFollowingsCount(userId: Int) {
        val response = friendshipService.fetchFollowersAndFollowingsCount(userId)
        FriendshipStore.setFollowersAndFollowingsCount(response)
    }
}
