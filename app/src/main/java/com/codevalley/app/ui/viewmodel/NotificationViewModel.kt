package com.codevalley.app.ui.viewmodel

import android.app.Notification
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.NotificationDto
import com.codevalley.app.model.NotificationType
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.repository.NotificationRepository
import com.codevalley.app.store.NotificationStore
import com.codevalley.app.store.PostStore
import com.codevalley.app.store.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val userProfile: UserResponseDTO?
        get() = UserStore.userProfile

    val notifications = NotificationStore.notifications
    val unreadNotificationsCount = NotificationStore.unreadNotificationsCount
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun loadNotifications() {
        viewModelScope.launch {
            isLoading = true
            try {
                val newNotifications = notificationRepository.getNotifications(50)
                NotificationStore.setNotifications(newNotifications)
                errorMessage = ""
            } catch (e: Exception) {
                errorMessage = "Failed to load notifications."
            }
            isLoading = false
        }
    }

    fun loadUnreadNotificationsCount() {
        viewModelScope.launch {
            try {
                val notificationCountDto = notificationRepository.getNotificationCount()
                NotificationStore.setUnreadNotificationsCount(notificationCountDto.count)
                errorMessage = ""
            } catch (e: Exception) {
                NotificationStore.setUnreadNotificationsCount(0)
                errorMessage = "Failed to load notification count."
            }
        }
    }

    fun seeNotification(notificationId: Int)
    {
        if (NotificationStore.getHasBeenRead(notificationId)) {
            return
        }
        viewModelScope.launch {
            NotificationStore.seeNotification(notificationId)
            try {
                notificationRepository.seeNotification(notificationId)
                NotificationStore.decrementUnreadNotificationsCount()
                errorMessage = ""
            } catch (e: Exception) {
                NotificationStore.unseeNotification(notificationId)
                errorMessage = "Failed to see notification."
            }
        }
    }

    fun unseeNotification(notificationId: Int)
    {
        if (!NotificationStore.getHasBeenRead(notificationId)) {
            return
        }
        viewModelScope.launch {
            NotificationStore.unseeNotification(notificationId)
            try {
                notificationRepository.unseeNotification(notificationId)
                NotificationStore.incrementUnreadNotificationsCount()
                errorMessage = ""
            } catch (e: Exception) {
                NotificationStore.seeNotification(notificationId)
                errorMessage = "Failed to unsee notification."
            }
        }
    }

    fun deleteNotification(notificationId: Int) {
        viewModelScope.launch {
            NotificationStore.deleteNotification(notificationId)
            try {
                notificationRepository.deleteNotification(notificationId)
                NotificationStore.decrementUnreadNotificationsCount()
                errorMessage = ""
            } catch (e: Exception) {
                NotificationStore.setNotifications(NotificationStore.notifications.value)
                errorMessage = "Failed to delete notification."
            }
        }
    }

    fun formatMessage(notification: NotificationDto): String {
        return when (notification.notificationType) {
            NotificationType.friendshipReceived -> "${notification.fromUsername} sent you a friend request!"
            NotificationType.friendshipAccepted -> "${notification.fromUsername} accepted your friend request!"
            NotificationType.friendshipRefused -> "${notification.fromUsername} refused your friend request..."
            NotificationType.post -> "Check the new post of ${notification.fromUsername} !"
            NotificationType.like -> "${notification.fromUsername} liked your post!"
        }
    }

    fun formatDate(date: Date): String {
        return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date)
    }
}
