package com.codevalley.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
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
import com.codevalley.app.model.MessageResponseDTO
import com.codevalley.app.ui.viewmodel.MessagesViewModel
import com.codevalley.app.store.UserStore
import com.codevalley.app.ui.components.MessageItem
import com.codevalley.app.ui.navigation.ScreenName

@Composable
fun MessageScreen(
    navController: NavController,
    groupId: Int,
    messagesViewModel: MessagesViewModel = hiltViewModel()
) {
    val messages by messagesViewModel.messages.collectAsState()
    val loading by messagesViewModel.loading.collectAsState()
    val error by messagesViewModel.error.collectAsState()
    val group by messagesViewModel.group.collectAsState()
    val context = LocalContext.current
    val currentUser = UserStore.currentUser.collectAsState()

    var messageText by remember { mutableStateOf("") }
    var showAdminMenu by remember { mutableStateOf(false) }

    LaunchedEffect(groupId) {
        messagesViewModel.loadMessages(groupId)
    }

    val isAdmin = remember(group, currentUser.value) {
        currentUser.value?.id?.let { messagesViewModel.isAdmin(it) } == true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Group Messages") },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isAdmin) {
                        IconButton(onClick = {
                            showAdminMenu = true
                        }) {
                            Icon(Icons.Default.Settings, contentDescription = "Group Management")
                        }
                        DropdownMenu(
                            expanded = showAdminMenu,
                            onDismissRequest = { showAdminMenu = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                showAdminMenu = false
                                navController.navigate("${ScreenName.GroupMembers}/$groupId")
                            }) {
                                Text("View Members")
                            }
                            DropdownMenuItem(onClick = {
                                showAdminMenu = false
                                navController.navigate("${ScreenName.JoinRequests}/$groupId")
                            }) {
                                Text("View Join Requests")
                            }
                            DropdownMenuItem(onClick = {
                                showAdminMenu = false
                                navController.navigate("${ScreenName.EditGroup}/$groupId")
                            }) {
                                Text("Edit Group")
                            }
                        }
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
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(messages) { message ->
                            MessageItem(message = message)
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Enter your message") }
                    )
                    Button(onClick = {
                        if (messageText.isNotBlank()) {
                            messagesViewModel.sendMessage(groupId, messageText)
                            messageText = ""
                        } else {
                            Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Send")
                    }
                }
            }
        }
    )
}