package com.codevalley.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
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
fun FollowingScreen(
    navController: NavController,
    userId: Int,
    currentUserId: Int,
    followingViewModel: FollowingViewModel = hiltViewModel()
) {
    val following by followingViewModel.following.collectAsState()
    val errorMessage by followingViewModel.errorMessage.collectAsState()
    val isLoading by followingViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        followingViewModel.loadFollowing(userId)
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
            Box(
                modifier = Modifier
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
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                    ) {
                        items(following) { user ->
                            UserItem(user = user, onRemove = {
                                followingViewModel.unfollowUser(user.id)
                            })
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { followingViewModel.loadFollowing(userId, offset = following.size) },
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
fun UserItem(user: UserItemDTO.UserFriend, onRemove: (() -> Unit)? = null) {
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
            IconButton(onClick = onRemove!!) {
                Icon(Icons.Filled.Clear, contentDescription = "Remove")
            }
        }
    }
}
