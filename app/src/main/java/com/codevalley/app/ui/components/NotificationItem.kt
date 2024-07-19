package com.codevalley.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codevalley.app.model.NotificationDto
import com.codevalley.app.model.NotificationType
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.NotificationViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun NotificationItem(notificationDto: NotificationDto, navController: NavController, notificationViewModel: NotificationViewModel = hiltViewModel()) {

    var showMenu by remember { mutableStateOf(false) }
    var showFriendRequestButton by remember { mutableStateOf(true) }

    val backgroundColor: Color
    val fontColor: Color
    val fontWeight: FontWeight

    if (notificationDto.hasBeenRead) {
        backgroundColor = Color.LightGray
        fontColor = Color.DarkGray
        fontWeight = FontWeight.Normal
    }
    else {
        backgroundColor = Color.White
        fontColor = Color.Black
        fontWeight = FontWeight.Bold
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                coroutineScope {
                    launch {
                        detectTapGestures(
                            onTap = {
                                notificationViewModel.seeNotification(notificationDto.id)
                            },
                            onLongPress = {
                                showMenu = true
                            }
                        )
                    }
                }
            },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = notificationViewModel.formatMessage(notificationDto),
                fontSize = 16.sp,
                color = fontColor,
                fontWeight = fontWeight
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (notificationViewModel.listIsFriendRequest.contains(notificationDto.id) && showFriendRequestButton) {
                Row {
                    Button(onClick = {
                        notificationViewModel.acceptFriendRequest(notificationDto)
                        notificationViewModel.seeNotification(notificationDto.id)
                        showFriendRequestButton = false
                    }) {
                        Text(text = "Accept")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        notificationViewModel.declineFriendRequest(notificationDto)
                        notificationViewModel.seeNotification(notificationDto.id)
                        showFriendRequestButton = false
                    }) {
                        Text(text = "Refuse")
                    }
                }
            }
            else if (notificationDto.notificationType == NotificationType.post || notificationDto.notificationType == NotificationType.like) {
                Button(onClick = {
                    notificationViewModel.seeNotification(notificationDto.id)
                    navController.navigate("${ScreenName.PostDetail}/${notificationDto.linkId}")
                }) {
                    Text(text = "Check post")
                }
            }
            Text(
                text = notificationViewModel.formatDate(notificationDto.createdAt),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    notificationViewModel.deleteNotification(notificationDto.id)
                    showMenu = false
                }
            ) {
                Text("Delete notification")
            }
            if (notificationDto.hasBeenRead) {
                DropdownMenuItem(
                    onClick = {
                        notificationViewModel.unseeNotification(notificationDto.id)
                        showMenu = false
                    }
                ) {
                    Text("Mark as unseen")
                }
            }
        }
    }
}
