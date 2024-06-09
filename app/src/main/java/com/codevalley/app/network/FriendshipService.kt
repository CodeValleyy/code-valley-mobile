package com.codevalley.app.network

import com.codevalley.app.model.FriendshipResponseDTO
import com.codevalley.app.model.RawFriendshipResponseDTO
import com.codevalley.app.model.UserItemDTO
import com.codevalley.app.model.UserQueryDTO
import retrofit2.http.*

interface FriendshipService {
    @POST("/friendships/send/{receiverId}")
    suspend fun sendFriendRequest(@Path("receiverId") receiverId: Int): FriendshipResponseDTO

    @POST("/friendships/accept/{friendshipId}")
    suspend fun acceptFriendRequest(@Path("friendshipId") friendshipId: Int): FriendshipResponseDTO

    @POST("/friendships/decline/{friendshipId}")
    suspend fun declineFriendRequest(@Path("friendshipId") friendshipId: Int)

    @DELETE("/friendships/remove/{friendId}")
    suspend fun removeFriend(@Path("friendId") friendId: Int)

    @GET("/friendships/requests")
    suspend fun listPendingRequests(): List<UserItemDTO.FriendshipPending>

    @GET("/friendships/sent-requests")
    suspend fun listSentRequests(): List<UserItemDTO.FriendshipSent>

    @DELETE("/friendships/requests/{receiverId}")
    suspend fun cancelFriendRequest(@Path("receiverId") receiverId: Int)

    @GET("/friendships/list")
    suspend fun listFriends(): List<UserItemDTO.UserFriend>

    @GET("/friendships/list/{userId}")
    suspend fun listFriendsById(@Path("userId") userId: Int): List<UserItemDTO.UserFriend>

    @GET("/friendships/status")
    suspend fun getFriendshipStatus(@Query("friendId") friendId: Int): RawFriendshipResponseDTO

    @GET("/friendships/suggestions")
    suspend fun listFriendSuggestions(): List<UserQueryDTO>
}
