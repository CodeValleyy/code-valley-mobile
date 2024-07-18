package com.codevalley.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Groups") },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                elevation = 12.dp
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Handle group creation
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
    }
}

@Composable
fun GroupItem(group: GroupResponseDTO, currentUserId: Int?, onJoinClick: () -> Unit) {
    val isMember = group.members.any { it.id == currentUserId }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(group.avatar),
            contentDescription = "Group Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { if (!isMember) onJoinClick() }
        ) {
            Text(text = group.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            group.description?.let {
                Text(text = it, fontSize = 14.sp, color = Color.Gray)
            }
        }
        if (!isMember) {
            Button(
                onClick = onJoinClick,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "Join")
            }
        }
    }
}
