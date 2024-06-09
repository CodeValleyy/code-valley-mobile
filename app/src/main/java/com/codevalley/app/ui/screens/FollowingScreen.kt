package com.codevalley.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codevalley.app.model.UserItemDTO
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.viewmodel.FollowingViewModel

@Composable
fun FollowingScreen(navController: NavController, userId: Int, currentUserId: Int, followingViewModel: FollowingViewModel = hiltViewModel()) {
    val following by followingViewModel.following.collectAsState()
    val sentRequests by followingViewModel.sentRequests.collectAsState()
    val errorMessage by followingViewModel.errorMessage.collectAsState()
    val isLoading by followingViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        followingViewModel.loadFollowing()
        followingViewModel.loadSentRequests()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Following") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
            ) {
                if (isLoading) {
                    LoadingIndicator()
                } else {
                    if (errorMessage.isNotEmpty()) {
                        Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize().background(Color.LightGray)
                    ) {
                        if (currentUserId == userId) {
                            item {
                                Text(
                                    "Sent Requests",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(sentRequests) { friendshipSent ->
                                UserItem(user = friendshipSent, isPending = true, onCancel = {
                                    followingViewModel.cancelRequest(friendshipSent.receiverId)
                                })
                            }
                        }
                        item {
                            Text("Friends", fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                        }
                        items(following) { user ->
                            UserItem(user = user, isPending = false) {
                                followingViewModel.removeFriend(user.id)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UserItem(user: UserItemDTO.FriendshipSent, isPending: Boolean, onCancel: (() -> Unit)? = null, onRemove: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(user.username, fontWeight = FontWeight.Bold)
                Text(user.email, style = MaterialTheme.typography.body2)
            }
            if (isPending) {
                Button(onClick = onCancel!!) {
                    Text("Cancel")
                }
            } else {
                Button(onClick = onRemove!!) {
                    Text("Remove")
                }
            }
        }
    }
}
