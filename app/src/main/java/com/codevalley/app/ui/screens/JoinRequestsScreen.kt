package com.codevalley.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.ui.viewmodel.JoinRequestsViewModel
import com.codevalley.app.store.UserStore
import com.codevalley.app.ui.components.JoinRequestItem
import com.codevalley.app.ui.navigation.ScreenName

@Composable
fun JoinRequestsScreen(
    navController: NavController,
    groupId: Int,
    joinRequestsViewModel: JoinRequestsViewModel = hiltViewModel()
) {
    val joinRequests by joinRequestsViewModel.joinRequests.collectAsState()
    val loading by joinRequestsViewModel.loading.collectAsState()
    val error by joinRequestsViewModel.error.collectAsState()
    val context = LocalContext.current
    val currentUser = UserStore.currentUser.collectAsState()

    LaunchedEffect(groupId) {
        joinRequestsViewModel.loadJoinRequests(groupId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Join Requests") },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {

                if (loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(joinRequests) { user ->
                            JoinRequestItem(
                                user = user,
                                currentUserId = currentUser.value?.id,
                                onProfileClick = {
                                    navController.navigate("${ScreenName.Profile}/${user.id}")
                                },
                                onAcceptClick = {
                                    joinRequestsViewModel.acceptJoinRequest(groupId, user.id)
                                },
                                onRefuseClick = {
                                    joinRequestsViewModel.refuseJoinRequest(groupId, user.id)
                                }
                            )
                        }
                    }
                }

                error?.let {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.error,
                        )
                    }
                }
            }
        }
    )
}