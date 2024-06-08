package com.codevalley.app.repository

import com.codevalley.app.model.FriendshipResponseDTO
import com.codevalley.app.model.FriendshipStatus
import com.codevalley.app.model.UserFriendDTO
import com.codevalley.app.model.UserQueryDTO
import com.codevalley.app.network.FriendshipService
import com.codevalley.app.network.createAuthorizedApiService
import retrofit2.Retrofit
import javax.inject.Inject

class FriendshipRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    private fun createAuthorizedApiService(): FriendshipService {
        return createAuthorizedApiService(retrofit, FriendshipService::class.java)
    }

    suspend fun sendFriendRequest(receiverId: Int): FriendshipResponseDTO {
        val friendshipService = createAuthorizedApiService()
        return friendshipService.sendFriendRequest(receiverId)
    }

    suspend fun acceptFriendRequest(friendshipId: Int): FriendshipResponseDTO {
        val friendshipService = createAuthorizedApiService()
        return friendshipService.acceptFriendRequest(friendshipId)
    }

    suspend fun declineFriendRequest(friendshipId: Int) {
        val friendshipService = createAuthorizedApiService()
        friendshipService.declineFriendRequest(friendshipId)
    }

    suspend fun removeFriend(friendId: Int) {
        val friendshipService = createAuthorizedApiService()
        friendshipService.removeFriend(friendId)
    }

    suspend fun listPendingRequests(): List<UserFriendDTO> {
        val friendshipService = createAuthorizedApiService()
        return friendshipService.listPendingRequests()
    }

    suspend fun listSentRequests(): List<UserFriendDTO> {
        val friendshipService = createAuthorizedApiService()
        return friendshipService.listSentRequests()
    }

    suspend fun cancelFriendRequest(receiverId: Int) {
        val friendshipService = createAuthorizedApiService()
        friendshipService.cancelFriendRequest(receiverId)
    }

    suspend fun listFriends(): List<UserFriendDTO> {
        val friendshipService = createAuthorizedApiService()
        return friendshipService.listFriends()
    }

    suspend fun getFriendshipStatus(friendId: Int): FriendshipResponseDTO {
        val friendshipService = createAuthorizedApiService()
        val response = friendshipService.getFriendshipStatus(friendId)
        return response.toFriendshipResponseDTO()
    }

    suspend fun listFriendSuggestions(): List<UserQueryDTO> {
        val friendshipService = createAuthorizedApiService()
        return friendshipService.listFriendSuggestions()
    }
}
