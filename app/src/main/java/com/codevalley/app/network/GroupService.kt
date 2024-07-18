package com.codevalley.app.network

import com.codevalley.app.model.GroupResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface GroupService {

    @Multipart
    @POST("/groups/create")
    suspend fun createGroup(
        @Part("groupDTO") groupDTO: RequestBody,
        @Part file: MultipartBody.Part?
    ): GroupResponseDTO

    @Multipart
    @PATCH("/groups/update/{groupId}")
    suspend fun updateGroup(
        @Path("groupId") groupId: Int,
        @Part("groupDTO") updateGroupDTO: RequestBody,
        @Part file: MultipartBody.Part?
    ): GroupResponseDTO

    @POST("/groups/add/{groupId}/{userId}")
    suspend fun addUserToGroup(
        @Path("groupId") groupId: Int,
        @Path("userId") userId: Int
    ): GroupResponseDTO

    @POST("/groups/admin/{groupId}/{userId}")
    suspend fun addAdmin(
        @Path("groupId") groupId: Int,
        @Path("userId") userId: Int
    ): GroupResponseDTO

    @POST("/groups/join/{groupId}/{userId}")
    suspend fun sendJoinRequest(
        @Path("groupId") groupId: Int,
        @Path("userId") userId: Int
    ): GroupResponseDTO

    @POST("/groups/accept/{groupId}/{userId}")
    suspend fun acceptJoinRequest(
        @Path("groupId") groupId: Int,
        @Path("userId") userId: Int
    ): GroupResponseDTO

    @DELETE("/groups/refuse/{groupId}/{userId}")
    suspend fun refuseJoinRequest(
        @Path("groupId") groupId: Int,
        @Path("userId") userId: Int
    )

    @DELETE("/groups/remove/{groupId}/{userId}")
    suspend fun removeUserFromGroup(
        @Path("groupId") groupId: Int,
        @Path("userId") userId: Int
    )

    @GET("/groups/list")
    suspend fun listGroups(): List<GroupResponseDTO>

    @GET("/groups/search/{name}")
    suspend fun searchProfile(
        @Path("name") name: String
    ): List<GroupResponseDTO>

    @GET("/groups/details/{groupId}")
    suspend fun getGroupDetails(
        @Path("groupId") groupId: Int
    ): GroupResponseDTO?
}
