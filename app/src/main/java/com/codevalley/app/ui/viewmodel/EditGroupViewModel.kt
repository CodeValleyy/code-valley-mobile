package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.GroupDTO
import com.codevalley.app.model.GroupResponseDTO
import com.codevalley.app.repository.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class EditGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _groupDetails = MutableStateFlow<GroupResponseDTO?>(null)
    val groupDetails: StateFlow<GroupResponseDTO?> = _groupDetails.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadGroupDetails(groupId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val group = groupRepository.getGroupDetails(groupId)
                _groupDetails.value = group
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateGroup(groupId: Int, updateGroupDTO: GroupDTO, file: MultipartBody.Part?) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                groupRepository.updateGroup(groupId, updateGroupDTO, file)
                loadGroupDetails(groupId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
