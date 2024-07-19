package com.codevalley.app.repository

import com.codevalley.app.model.MessageDTO
import com.codevalley.app.model.MessageResponseDTO
import com.codevalley.app.network.MessageService
import com.codevalley.app.network.createAuthorizedApiService
import retrofit2.Retrofit
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    private fun createAuthorizedApiService(): MessageService {
        return createAuthorizedApiService(retrofit, MessageService::class.java)
    }

    suspend fun createMessage(messageDTO: MessageDTO): MessageResponseDTO {
        val messageService = createAuthorizedApiService()
        return messageService.createMessage(messageDTO)
    }

    suspend fun listMessages(): List<MessageResponseDTO> {
        val messageService = createAuthorizedApiService()
        return messageService.listMessages()
    }

    suspend fun getMessagesByGroupId(groupId: Int): List<MessageResponseDTO> {
        val messageService = createAuthorizedApiService()
        return messageService.getMessagesByGroupId(groupId)
    }
}
