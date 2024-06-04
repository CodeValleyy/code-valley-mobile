package com.codevalley.app.repository

import com.codevalley.app.model.GroupResponseDTO
import com.codevalley.app.model.GroupDTO
import com.codevalley.app.network.AuthService
import com.codevalley.app.network.GroupService
import com.codevalley.app.network.createAuthorizedApiService
import retrofit2.Retrofit
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    private fun createAuthorizedApiService(token: String): GroupService {
        return createAuthorizedApiService(token, retrofit, GroupService::class.java)
    }
        suspend fun createGroup(groupDTO: GroupDTO, token: String): GroupResponseDTO {
        val groupService = createAuthorizedApiService(token)
        return groupService.createGroup(groupDTO)
    }

    suspend fun addUserToGroup(groupId: Int, userId: Int, token: String): GroupResponseDTO {
        val groupService = createAuthorizedApiService(token)
        return groupService.addUserToGroup(groupId, userId)
    }

    suspend fun removeUserFromGroup(groupId: Int, userId: Int, token: String) {
        val groupService = createAuthorizedApiService(token)
        groupService.removeUserFromGroup(groupId, userId)
    }

    suspend fun listGroups(token: String): List<GroupResponseDTO> {
        val groupService = createAuthorizedApiService(token)
        return groupService.listGroups()
    }

    suspend fun getGroupDetails(groupId: Int, token: String): GroupResponseDTO {
        val groupService = createAuthorizedApiService(token)
        return groupService.getGroupDetails(groupId)
    }
}
