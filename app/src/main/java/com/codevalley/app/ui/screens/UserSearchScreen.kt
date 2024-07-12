package com.codevalley.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.UserSearchViewModel

@Composable
fun UserSearchScreen(navController: NavController, userSearchViewModel: UserSearchViewModel = hiltViewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val users by userSearchViewModel.users.collectAsState()
    val isLoading by userSearchViewModel.isLoading.collectAsState()
    val errorMessage by userSearchViewModel.errorMessage.collectAsState()
    var canNavigateBack by remember { mutableStateOf(true) }
    val canNavigateBackState by rememberUpdatedState(canNavigateBack)

    LaunchedEffect(canNavigateBackState) {
        if (!canNavigateBackState) {
            kotlinx.coroutines.delay(1000)
            canNavigateBack = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Users") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (canNavigateBack) {
                                navController.popBackStack()
                                canNavigateBack = false
                                if (canNavigateBack) {
                                    navController.popBackStack()
                                    canNavigateBack = false
                                }
                            }
                        },
                        enabled = canNavigateBack
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search users...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
                Button(
                    onClick = { userSearchViewModel.searchUsers(searchQuery) },
                    modifier = Modifier.align(Alignment.End).padding(16.dp)
                ) {
                    Text("Search")
                }

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(users) { user ->
                            UserItem(user = user, navController = navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UserItem(user: UserResponseDTO, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("${ScreenName.Profile}/${user.id}") },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.avatar ?: ""),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(user.username, fontWeight = FontWeight.Bold)
            Text(user.email, style = MaterialTheme.typography.body2)
        }
    }
}
