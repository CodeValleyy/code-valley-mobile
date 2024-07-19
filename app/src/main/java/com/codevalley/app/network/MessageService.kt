package com.codevalley.app.network

import com.codevalley.app.model.MessageDTO
import com.codevalley.app.model.MessageResponseDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageService {

    @POST("/messages/create")
    suspend fun createMessage(@Body messageDTO: MessageDTO): MessageResponseDTO

    @GET("/messages/list")
    suspend fun listMessages(): List<MessageResponseDTO>

    @GET("/messages/conversation/{groupId}")
    suspend fun getMessagesByGroupId(@Path("groupId") groupId: Int): List<MessageResponseDTO>
}
