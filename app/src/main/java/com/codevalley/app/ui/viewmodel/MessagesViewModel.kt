package com.codevalley.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.MessageDTO
import com.codevalley.app.model.MessageResponseDTO
import com.codevalley.app.model.GroupResponseDTO
import com.codevalley.app.repository.MessageRepository
import com.codevalley.app.repository.GroupRepository
import com.codevalley.app.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val groupRepository: GroupRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<MessageResponseDTO>>(emptyList())
    val messages: StateFlow<List<MessageResponseDTO>> = _messages.asStateFlow()

    private val _group = MutableStateFlow<GroupResponseDTO?>(null)
    val group: StateFlow<GroupResponseDTO?> = _group.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadMessages(groupId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val messages = messageRepository.getMessagesByGroupId(groupId)
                _messages.value = messages

                val group = groupRepository.getGroupDetails(groupId)
                _group.value = group
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun sendMessage(groupId: Int, message: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val userId = UserStore.currentUser.value?.id ?: return@launch
                val messageDTO = MessageDTO(
                    value = message,
                    authorId = userId.toString(),
                    groupId = groupId.toString()
                )
                messageRepository.createMessage(messageDTO)
                loadMessages(groupId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun isAdmin(userId: Int): Boolean {
        return _group.value?.admins?.any { it.id == userId } == true
    }
}
