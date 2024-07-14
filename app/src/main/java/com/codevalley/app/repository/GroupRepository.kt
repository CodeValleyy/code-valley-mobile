package com.codevalley.app.repository

import com.codevalley.app.model.GroupResponseDTO
import com.codevalley.app.model.GroupDTO
import com.codevalley.app.network.GroupService
import com.codevalley.app.network.createAuthorizedApiService
import retrofit2.Retrofit
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    private fun createAuthorizedApiService(): GroupService {
        return createAuthorizedApiService(retrofit, GroupService::class.java)
    }

    suspend fun createGroup(groupDTO: GroupDTO): GroupResponseDTO {
        val groupService = createAuthorizedApiService()
        return groupService.createGroup(groupDTO)
    }

    suspend fun addUserToGroup(groupId: Int, userId: Int): GroupResponseDTO {
        val groupService = createAuthorizedApiService()
        return groupService.addUserToGroup(groupId, userId)
    }

    suspend fun removeUserFromGroup(groupId: Int, userId: Int) {
        val groupService = createAuthorizedApiService()
        groupService.removeUserFromGroup(groupId, userId)
    }

    suspend fun listGroups(): List<GroupResponseDTO> {
        val groupService = createAuthorizedApiService()
        return groupService.listGroups()
    }

    suspend fun getGroupDetails(groupId: Int): GroupResponseDTO {
        val groupService = createAuthorizedApiService()
        return groupService.getGroupDetails(groupId)
    }
}
