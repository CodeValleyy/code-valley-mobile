package com.codevalley.app.repository

import com.codevalley.app.model.FriendshipResponseDTO
import com.codevalley.app.model.UserFriendDTO
import com.codevalley.app.model.UserQueryDTO
import com.codevalley.app.network.FriendshipService
import com.codevalley.app.network.createAuthorizedApiService
import retrofit2.Retrofit
import javax.inject.Inject

class FriendshipRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun sendFriendRequest(receiverId: Int, token: String): FriendshipResponseDTO {
        val friendshipService = createAuthorizedApiService(token, retrofit, FriendshipService::class.java)
        return friendshipService.sendFriendRequest(receiverId)
    }

    suspend fun acceptFriendRequest(friendshipId: Int, token: String): FriendshipResponseDTO {
        val friendshipService = createAuthorizedApiService(token, retrofit, FriendshipService::class.java)
        return friendshipService.acceptFriendRequest(friendshipId)
    }

    suspend fun declineFriendRequest(friendshipId: Int, token: String) {
        val friendshipService = createAuthorizedApiService(token, retrofit, FriendshipService::class.java)
        friendshipService.declineFriendRequest(friendshipId)
    }

    suspend fun removeFriend(friendId: Int, token: String) {
        val friendshipService = createAuthorizedApiService(token, retrofit, FriendshipService::class.java)
        friendshipService.removeFriend(friendId)
    }

    suspend fun listPendingRequests(token: String): List<UserFriendDTO> {
        val friendshipService = createAuthorizedApiService(token, retrofit, FriendshipService::class.java)
        return friendshipService.listPendingRequests()
    }

    suspend fun listSentRequests(token: String): List<UserFriendDTO> {
        val friendshipService = createAuthorizedApiService(token, retrofit, FriendshipService::class.java)
        return friendshipService.listSentRequests()
    }

    suspend fun cancelFriendRequest(receiverId: Int, token: String) {
        val friendshipService = createAuthorizedApiService(token, retrofit, FriendshipService::class.java)
        friendshipService.cancelFriendRequest(receiverId)
    }

    suspend fun listFriends(token: String): List<UserFriendDTO> {
        val friendshipService = createAuthorizedApiService(token, retrofit, FriendshipService::class.java)
        return friendshipService.listFriends()
    }

    suspend fun getFriendshipStatus(friendId: Int, token: String): FriendshipResponseDTO {
        val friendshipService = createAuthorizedApiService(token, retrofit, FriendshipService::class.java)
        return friendshipService.getFriendshipStatus(friendId)
    }

    suspend fun listFriendSuggestions(token: String): List<UserQueryDTO> {
        val friendshipService = createAuthorizedApiService(token, retrofit, FriendshipService::class.java)
        return friendshipService.listFriendSuggestions()
    }
}
