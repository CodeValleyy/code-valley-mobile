package com.codevalley.app.ui.screens

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codevalley.app.ui.viewmodel.GroupMembersViewModel
import com.codevalley.app.store.UserStore
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.components.MemberItem
import com.codevalley.app.ui.navigation.ScreenName

@Composable
fun GroupMembersScreen(
    navController: NavController,
    groupId: Int,
    groupMembersViewModel: GroupMembersViewModel = hiltViewModel()
) {
    val members by groupMembersViewModel.members.collectAsState()
    val loading by groupMembersViewModel.loading.collectAsState()
    val error by groupMembersViewModel.error.collectAsState()
    val context = LocalContext.current
    val currentUser = UserStore.currentUser.collectAsState()
    val currentUserIsAdmin by remember { derivedStateOf { members.any { it.id == currentUser.value?.id} } }

    LaunchedEffect(groupId) {
        groupMembersViewModel.loadMembers(groupId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Group Members") },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {

                if (loading) {
                    LoadingIndicator()
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(members) { member ->
                            MemberItem(
                                member = member,
                                currentUserId = currentUser.value?.id,
                                currentUserIsAdmin = currentUserIsAdmin,
                                memberIsAdmin = false,
                                onProfileClick = {
                                    navController.navigate("${ScreenName.Profile}/${member.id}")
                                },
                                onRemoveClick = {
                                    groupMembersViewModel.removeMember(groupId, member.id)
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