package com.codevalley.app.network

import com.codevalley.app.model.FollowersAndFollowingsCount
import com.codevalley.app.model.FriendshipResponseDTO
import com.codevalley.app.model.RawFriendshipResponseDTO
import com.codevalley.app.model.UserItemDTO
import com.codevalley.app.model.UserQueryDTO
import retrofit2.http.*

interface FriendshipService {
    @POST("/friendships/send/{receiverId}")
    suspend fun sendFriendRequest(@Path("receiverId") receiverId: Int): FriendshipResponseDTO

    @POST("/friendships/accept/{senderId}")
    suspend fun acceptFriendRequest(@Path("senderId") friendshipId: Int): FriendshipResponseDTO

    @POST("/friendships/decline/{senderId}")
    suspend fun declineFriendRequest(@Path("senderId") friendshipId: Int)

    @DELETE("/friendships/remove/{friendId}")
    suspend fun removeFriend(@Path("friendId") friendId: Int)

    @GET("/friendships/requests")
    suspend fun listPendingRequests(): List<UserItemDTO.FriendshipPending>

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

    @GET("/friendships/following/{currentUserId}/{targetUserId}")
    suspend fun isFollowing(@Path("currentUserId") currentUserId: Int, @Path("targetUserId") targetUserId: Int): Boolean

    @GET("/friendships/followers/{userId}")
    suspend fun listFollowers(
        @Path("userId") userId: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<UserItemDTO.UserFriend>

    @GET("/friendships/followings/{userId}")
    suspend fun listFollowing(
        @Path("userId") userId: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<UserItemDTO.UserFriend>
    @GET("/friendships/count/{userId}")
    suspend fun fetchFollowersAndFollowingsCount(@Path("userId") userId: Int): FollowersAndFollowingsCount
}
