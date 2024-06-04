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

    suspend fun createGroup(groupDTO: GroupDTO, token: String): GroupResponseDTO {
        val groupService = createAuthorizedApiService(token, retrofit, GroupService::class.java)
        return groupService.createGroup(groupDTO)
    }

    suspend fun addUserToGroup(groupId: Int, userId: Int, token: String): GroupResponseDTO {
        val groupService = createAuthorizedApiService(token, retrofit, GroupService::class.java)
        return groupService.addUserToGroup(groupId, userId)
    }

    suspend fun removeUserFromGroup(groupId: Int, userId: Int, token: String) {
        val groupService = createAuthorizedApiService(token, retrofit, GroupService::class.java)
        groupService.removeUserFromGroup(groupId, userId)
    }

    suspend fun listGroups(token: String): List<GroupResponseDTO> {
        val groupService = createAuthorizedApiService(token, retrofit, GroupService::class.java)
        return groupService.listGroups()
    }

    suspend fun getGroupDetails(groupId: Int, token: String): GroupResponseDTO {
        val groupService = createAuthorizedApiService(token, retrofit, GroupService::class.java)
        return groupService.getGroupDetails(groupId)
    }
}
