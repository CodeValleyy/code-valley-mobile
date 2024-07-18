package com.codevalley.app.repository

import com.codevalley.app.model.NotificationCountDto
import com.codevalley.app.model.NotificationDto
import com.codevalley.app.network.NotificationService
import com.codevalley.app.network.createAuthorizedApiService
import retrofit2.Retrofit
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val retrofit: Retrofit
) {

    private val notificationService: NotificationService by lazy {
        createAuthorizedApiService(retrofit, NotificationService::class.java)
    }

    suspend fun getNotificationCount(): NotificationCountDto {
        return notificationService.getNotificationCount()
    }

    suspend fun getNotifications(limit: Int): List<NotificationDto> {
        return notificationService.getNotifications(limit)
    }

    suspend fun seeNotification(id: Int): Boolean {
        try {
            notificationService.seeNotification(id)
            return true
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    suspend fun unseeNotification(id: Int): Boolean {
        try {
            notificationService.unseeNotification(id)
            return true
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    suspend fun deleteNotification(id: Int): Boolean {
        try {
            notificationService.deleteNotification(id)
            return true
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}