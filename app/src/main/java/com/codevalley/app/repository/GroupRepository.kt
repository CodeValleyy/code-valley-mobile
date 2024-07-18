package com.codevalley.app.repository

import com.codevalley.app.model.GroupDTO
import com.codevalley.app.model.GroupResponseDTO
import com.codevalley.app.network.GroupService
import com.codevalley.app.network.createAuthorizedApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    private fun createAuthorizedApiService(): GroupService {
        return createAuthorizedApiService(retrofit, GroupService::class.java)
    }

    suspend fun createGroup(groupDTO: GroupDTO, file: MultipartBody.Part?): GroupResponseDTO {
        val groupService = createAuthorizedApiService()

        val name = groupDTO.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val description = groupDTO.description.toRequestBody("text/plain".toMediaTypeOrNull())
        val isPublic = groupDTO.isPublic.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        return groupService.createGroup(name, description, isPublic, file)
    }

    suspend fun updateGroup(groupId: Int, updateGroupDTO: GroupDTO, file: MultipartBody.Part?): GroupResponseDTO {
        val groupService = createAuthorizedApiService()

        val name = updateGroupDTO.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val description = updateGroupDTO.description.toRequestBody("text/plain".toMediaTypeOrNull())
        val isPublic = updateGroupDTO.isPublic.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        return groupService.updateGroup(groupId, name, description, isPublic, file)
    }

    suspend fun addUserToGroup(groupId: Int, userId: Int): GroupResponseDTO {
        val groupService = createAuthorizedApiService()
        return groupService.addUserToGroup(groupId, userId)
    }

    suspend fun addAdmin(groupId: Int, userId: Int): GroupResponseDTO {
        val groupService = createAuthorizedApiService()
        return groupService.addAdmin(groupId, userId)
    }

    suspend fun sendJoinRequest(groupId: Int, userId: Int): GroupResponseDTO {
        val groupService = createAuthorizedApiService()
        return groupService.sendJoinRequest(groupId, userId)
    }

    suspend fun acceptJoinRequest(groupId: Int, userId: Int): GroupResponseDTO {
        val groupService = createAuthorizedApiService()
        return groupService.acceptJoinRequest(groupId, userId)
    }

    suspend fun refuseJoinRequest(groupId: Int, userId: Int) {
        val groupService = createAuthorizedApiService()
        groupService.refuseJoinRequest(groupId, userId)
    }

    suspend fun removeUserFromGroup(groupId: Int, userId: Int) {
        val groupService = createAuthorizedApiService()
        groupService.removeUserFromGroup(groupId, userId)
    }

    suspend fun listGroups(): List<GroupResponseDTO> {
        val groupService = createAuthorizedApiService()
        return groupService.listGroups()
    }

    suspend fun searchProfile(name: String): List<GroupResponseDTO> {
        val groupService = createAuthorizedApiService()
        return groupService.searchProfile(name)
    }

    suspend fun getGroupDetails(groupId: Int): GroupResponseDTO? {
        val groupService = createAuthorizedApiService()
        return groupService.getGroupDetails(groupId)
    }
}
