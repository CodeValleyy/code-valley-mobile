package com.codevalley.app.store

import com.codevalley.app.model.NotificationDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object NotificationStore {
    private val _notifications = MutableStateFlow<List<NotificationDto>>(emptyList())
    val notifications: StateFlow<List<NotificationDto>> = _notifications

    private val _unreadNotificationsCount = MutableStateFlow(0)
    val unreadNotificationsCount: StateFlow<Int> = _unreadNotificationsCount

    fun setNotifications(notifications: List<NotificationDto>) {
        _notifications.value = notifications
    }

    fun setUnreadNotificationsCount(count: Int) {
        _unreadNotificationsCount.value = count
    }

    fun incrementUnreadNotificationsCount() {
        _unreadNotificationsCount.value++
    }

    fun decrementUnreadNotificationsCount() {
        _unreadNotificationsCount.value--
    }

    fun seeNotification(notificationId: Int) {
        _notifications.value = _notifications.value.map {
            if (it.id == notificationId) {
                it.copy(hasBeenRead = true)
            } else {
                it
            }
        }
    }

    fun unseeNotification(notificationId: Int) {
        _notifications.value = _notifications.value.map {
            if (it.id == notificationId) {
                it.copy(hasBeenRead = false)
            } else {
                it
            }
        }
    }

    fun deleteNotification(notificationId: Int) {
        _notifications.value = _notifications.value.filter { it.id != notificationId }
    }
}