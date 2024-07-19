package com.codevalley.app.network

import com.codevalley.app.model.NotificationCountDto
import com.codevalley.app.model.NotificationDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationService {
    @GET("/notifications/count")
    suspend fun getNotificationCount(): NotificationCountDto

    @GET("/notifications/{limit}")
    suspend fun getNotifications(@Path("limit") limit: Int): List<NotificationDto>

    @POST("/notifications/see/{id}")
    suspend fun seeNotification(@Path("id") id: Int)

    @POST("/notifications/unsee/{id}")
    suspend fun unseeNotification(@Path("id") id: Int)

    @DELETE("/notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Int)
}
