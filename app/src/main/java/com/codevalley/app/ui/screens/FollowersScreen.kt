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
import com.codevalley.app.model.FriendshipStatus
import com.codevalley.app.model.UserItemDTO
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.viewmodel.FollowersViewModel

@Composable
fun FollowersScreen(
    navController: NavController,
    userId: Int,
    currentUserId: Int,
    followersViewModel: FollowersViewModel = hiltViewModel()
) {
    val followers by followersViewModel.followers.collectAsState()
    val errorMessage by followersViewModel.errorMessage.collectAsState()
    val isLoading by followersViewModel.isLoading.collectAsState()
    var offset by remember { mutableIntStateOf(0) }
    val limit = 10

    LaunchedEffect(Unit) {
        followersViewModel.loadFollowers(userId, limit, offset)
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (isLoading && followers.isEmpty()) {
                    LoadingIndicator()
                } else {
                    if (errorMessage.isNotEmpty()) {
                        Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                    ) {
                        items(followers) { user ->
                            UserItem(
                                user = user,
                                onAccept = { followersViewModel.acceptRequest(user.id) },
                                onRemove = if (currentUserId == userId) { { followersViewModel.removeFriend(user.id) } } else { null }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    offset += limit
                                    followersViewModel.loadFollowers(userId, limit, offset = followers.size)
                                },
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            ) {
                                Text("Load More")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UserItem(
    user: UserItemDTO.UserFriend,
    onAccept: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null
) {
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
            if (user.status == FriendshipStatus.PENDING) {
                Button(onClick = onAccept!!) {
                    Text("Accept")
                }
            } else {
                onRemove?.let {
                    Button(onClick = it) {
                        Text("Remove")
                    }
                }
            }
        }
    }
}
