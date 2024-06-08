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
import com.codevalley.app.model.UserFriendDTO
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.viewmodel.FollowersViewModel

@Composable
fun FollowersScreen(navController: NavController, followersViewModel: FollowersViewModel = hiltViewModel()) {
    val followers by followersViewModel.followers.collectAsState()
    val pendingRequests by followersViewModel.pendingRequests.collectAsState()
    val errorMessage by followersViewModel.errorMessage.collectAsState()
    val isLoading by followersViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        followersViewModel.loadFollowers()
        println("FollowersScreen: LaunchedEffect, followers: $followers")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Followers") },
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
                        item {
                            Text("Pending Requests", fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                        }
                        items(pendingRequests) { user ->
                            UserItem(user = user, isPending = true, onAccept = {
                                followersViewModel.acceptRequest(user.id)
                            }, onDecline = {
                                followersViewModel.declineRequest(user.id)
                            })
                        }
                        item {
                            Text("Friends", fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                        }
                        items(followers) { user ->
                            UserItem(user = user, isPending = false, onRemove = {
                                followersViewModel.removeFriend(user.id)
                            })
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UserItem(user: UserFriendDTO, isPending: Boolean, onAccept: (() -> Unit)? = null, onDecline: (() -> Unit)? = null, onRemove: (() -> Unit)? = null) {
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
                Button(onClick = onAccept!!) {
                    Text("Accept")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDecline!!) {
                    Text("Decline")
                }
            } else {
                Button(onClick = onRemove!!) {
                    Text("Remove")
                }
            }
        }
    }
}
