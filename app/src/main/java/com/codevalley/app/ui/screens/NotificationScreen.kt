package com.codevalley.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.codevalley.app.store.UserStore
import com.codevalley.app.ui.components.AvatarComponent
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.components.NotificationItem
import com.codevalley.app.ui.theme.CodeValleyTheme
import com.codevalley.app.ui.viewmodel.NotificationViewModel

@Composable
fun NotificationScreen(navController: NavController, notificationViewModel: NotificationViewModel = hiltViewModel()) {
    val notifications by notificationViewModel.notifications.collectAsState()
    val notificationCount by notificationViewModel.unreadNotificationsCount.collectAsState()
    val isLoading = notificationViewModel.isLoading
    val errorMessage = notificationViewModel.errorMessage
    val context = LocalContext.current
    val currentUser = UserStore.getUserProfile()

    LaunchedEffect(Unit) {
        notificationViewModel.loadUnreadNotificationsCount()
        notificationViewModel.loadNotifications()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Notifications (${notificationCount})")
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                modifier = Modifier.height(56.dp),
                actions = { AvatarComponent(navController, currentUser) }
            )
        },
        content = { padding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                if (errorMessage.isNotEmpty()) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
                if (isLoading) {
                    LoadingIndicator()
                }
                else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(notifications) { notification ->
                            NotificationItem(notification)
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    CodeValleyTheme {
        NotificationScreen(rememberNavController())
    }
}