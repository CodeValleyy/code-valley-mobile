package com.codevalley.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.ui.viewmodel.GroupsViewModel
import com.codevalley.app.model.GroupResponseDTO
import com.codevalley.app.store.UserStore
import com.codevalley.app.ui.components.CreateGroupDialog
import com.codevalley.app.ui.components.GroupItem
import com.codevalley.app.ui.navigation.ScreenName

@Composable
fun GroupsScreen(
    navController: NavController,
    groupsViewModel: GroupsViewModel = hiltViewModel()
) {
    val groups by groupsViewModel.groups.collectAsState()
    val loading by groupsViewModel.loading.collectAsState()
    val error by groupsViewModel.error.collectAsState()
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }
    val currentUser = UserStore.currentUser.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Groups") },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                elevation = 12.dp,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Create Group")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search Groups") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            if (loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val filteredGroups = groups.filter { it.name.contains(searchText, ignoreCase = true) }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredGroups) { group ->
                        GroupItem(
                            group = group,
                            currentUserId = currentUser.value?.id,
                            onJoinClick = {
                                groupsViewModel.joinGroup(
                                    group.id,
                                    group.isPublic,
                                    onAlreadyJoined = {
                                        Toast.makeText(context, "You have already joined this group", Toast.LENGTH_SHORT).show()
                                    },
                                    onRequestSent = {
                                        Toast.makeText(context, "Join request sent", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            },
                            onGroupClick = { groupId ->
                                navController.navigate("${ScreenName.Messages}/$groupId")
                            }
                        )
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

        if (showDialog) {
            CreateGroupDialog(
                onDismiss = { showDialog = false },
                onCreate = { groupDTO, file ->
                    groupsViewModel.createGroup(groupDTO, file)
                    showDialog = false
                }
            )
        }
    }
}
