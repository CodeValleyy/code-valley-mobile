package com.codevalley.app.repository

import com.codevalley.app.model.FriendshipResponseDTO
import com.codevalley.app.model.UserItemDTO
import com.codevalley.app.model.UserQueryDTO
import com.codevalley.app.network.FriendshipService
import com.codevalley.app.network.createAuthorizedApiService
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

    suspend fun acceptFriendRequest(friendshipId: Int): FriendshipResponseDTO {
        return friendshipService.acceptFriendRequest(friendshipId)
    }

    suspend fun declineFriendRequest(friendshipId: Int) {
        friendshipService.declineFriendRequest(friendshipId)
    }

    suspend fun removeFriend(friendId: Int) {
        friendshipService.removeFriend(friendId)
    }

    suspend fun listPendingRequests(): List<UserItemDTO.FriendshipPending> {
        return friendshipService.listPendingRequests()
    }

    suspend fun listSentRequests(): List<UserItemDTO.FriendshipSent> {
        return friendshipService.listSentRequests()
    }

    suspend fun cancelFriendRequest(receiverId: Int) {
        friendshipService.cancelFriendRequest(receiverId)
    }

    suspend fun listFriends(): List<UserItemDTO.UserFriend> {
        return friendshipService.listFriends()
    }

    suspend fun listFriendsById(friendId: Int): List<UserItemDTO.UserFriend> {
        return friendshipService.listFriendsById(friendId)
    }

    suspend fun getFriendshipStatus(friendId: Int): FriendshipResponseDTO {
        val response = friendshipService.getFriendshipStatus(friendId)
        return response.toFriendshipResponseDTO()
    }

    suspend fun listFriendSuggestions(): List<UserQueryDTO> {
        return friendshipService.listFriendSuggestions()
    }
}
