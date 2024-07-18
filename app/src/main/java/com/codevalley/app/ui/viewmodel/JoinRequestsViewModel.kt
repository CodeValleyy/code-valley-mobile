package com.codevalley.app.ui.viewmodel

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
class JoinRequestsViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _joinRequests = MutableStateFlow<List<UserResponseDTO>>(emptyList())
    val joinRequests: StateFlow<List<UserResponseDTO>> = _joinRequests.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadJoinRequests(groupId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val group = groupRepository.getGroupDetails(groupId)
                _joinRequests.value = group?.memberJoinRequests ?: emptyList()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun acceptJoinRequest(groupId: Int, userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                groupRepository.acceptJoinRequest(groupId, userId)
                loadJoinRequests(groupId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun refuseJoinRequest(groupId: Int, userId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                groupRepository.refuseJoinRequest(groupId, userId)
                loadJoinRequests(groupId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
