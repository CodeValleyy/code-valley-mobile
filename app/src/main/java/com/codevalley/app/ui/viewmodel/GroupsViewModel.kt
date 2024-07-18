package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.GroupDTO
import com.codevalley.app.model.GroupResponseDTO
import com.codevalley.app.repository.GroupRepository
import com.codevalley.app.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _groups = MutableStateFlow<List<GroupResponseDTO>>(emptyList())
    val groups: StateFlow<List<GroupResponseDTO>> = _groups.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val groups = groupRepository.listGroups()
                _groups.value = groups
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun createGroup(groupDTO: GroupDTO, file: MultipartBody.Part?) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                groupRepository.createGroup(groupDTO, file)
                loadGroups()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun joinGroup(groupId: Int, isPublic: Boolean, onAlreadyJoined: () -> Unit, onRequestSent: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val userId = UserStore.currentUser.value?.id ?: return@launch
                val group = _groups.value.firstOrNull { it.id == groupId }
                if (group != null && group.members.any { it.id == userId }) {
                    onAlreadyJoined()
                } else {
                    if (isPublic) {
                        groupRepository.addUserToGroup(groupId, userId)
                    } else {
                        groupRepository.sendJoinRequest(groupId, userId)
                    }
                    onRequestSent()
                    loadGroups()
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}