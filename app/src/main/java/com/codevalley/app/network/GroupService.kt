package com.codevalley.app.network

import com.codevalley.app.model.GroupDTO
import com.codevalley.app.model.GroupResponseDTO
import retrofit2.http.*

interface GroupService {
    @POST("/groups/create")
    suspend fun createGroup(@Body groupDTO: GroupDTO): GroupResponseDTO

    @POST("/groups/add/{groupId}/{userId}")
    suspend fun addUserToGroup(@Path("groupId") groupId: Int, @Path("userId") userId: Int): GroupResponseDTO

    @DELETE("/groups/remove/{groupId}/{userId}")
    suspend fun removeUserFromGroup(@Path("groupId") groupId: Int, @Path("userId") userId: Int)

    @GET("/groups/list")
    suspend fun listGroups(): List<GroupResponseDTO>

    @GET("/groups/details/{groupId}")
    suspend fun getGroupDetails(@Path("groupId") groupId: Int): GroupResponseDTO
}
