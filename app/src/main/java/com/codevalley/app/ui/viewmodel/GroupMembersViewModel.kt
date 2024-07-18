package com.codevalley.app.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupMembersViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _members = MutableStateFlow<List<UserResponseDTO>>(emptyList())
    val members: StateFlow<List<UserResponseDTO>> = _members.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadMembers(groupId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val group = groupRepository.getGroupDetails(groupId)
                _members.value = group?.members ?: emptyList()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun removeMember(groupId: Int, userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                groupRepository.removeUserFromGroup(groupId, userId)
                loadMembers(groupId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun makeAdmin(groupId: Int, userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                Log.d("GroupRepository", "addAdmin: $groupId, $userId")
                groupRepository.addAdmin(groupId, userId)
                loadMembers(groupId)
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
