package com.codevalley.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codevalley.app.model.FriendshipStatus
import com.codevalley.app.model.NotificationDto
import com.codevalley.app.model.NotificationType
import com.codevalley.app.repository.FriendshipRepository
import com.codevalley.app.repository.NotificationRepository
import com.codevalley.app.store.NotificationStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val friendshipRepository: FriendshipRepository
) : ViewModel() {

    val notifications = NotificationStore.notifications
    val unreadNotificationsCount = NotificationStore.unreadNotificationsCount
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var listIsFriendRequest by mutableStateOf(mutableListOf<Int>())

    fun loadNotifications() {
        viewModelScope.launch {
            isLoading = true
            try {
                val newNotifications = notificationRepository.getNotifications(50)
                NotificationStore.setNotifications(newNotifications)
                for (notification in newNotifications) {
                    if (notification.notificationType == NotificationType.friendshipReceived) {
                        isFriendRequestPending(notification)
                    }
                }
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

    fun acceptFriendRequest(notification: NotificationDto) {
        viewModelScope.launch {
            try {
                friendshipRepository.acceptFriendRequest(notification.fromUserId)
                listIsFriendRequest.remove(notification.fromUserId)
            } catch (e: Exception) {
                errorMessage = "Failed to accept friend request."
            }
        }
    }

    fun declineFriendRequest(notification: NotificationDto) {
        viewModelScope.launch {
            try {
                friendshipRepository.declineFriendRequest(notification.fromUserId)
                listIsFriendRequest.remove(notification.fromUserId)
            } catch (e: Exception) {
                errorMessage = "Failed to decline friend request."
            }
        }
    }

    private fun isFriendRequestPending(notification: NotificationDto) {
        viewModelScope.launch {
            try {
                val friendship = friendshipRepository.getFriendshipStatus(notification.fromUserId)
                if (friendship.status == FriendshipStatus.PENDING) {
                    listIsFriendRequest.add(notification.id)
                }
            } catch (e: Exception) {
                errorMessage = "Failed to check if friend request is pending."
            }
        }
    }

    fun isFriendRequest(notification: NotificationDto): Boolean {
        return listIsFriendRequest.contains(notification.id)
    }
}
