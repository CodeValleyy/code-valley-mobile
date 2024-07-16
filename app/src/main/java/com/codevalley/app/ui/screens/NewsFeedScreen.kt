package com.codevalley.app.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codevalley.app.model.CreatePostDto
import com.codevalley.app.store.PostStore
import com.codevalley.app.store.UserStore
import com.codevalley.app.ui.components.AvatarComponent
import com.codevalley.app.ui.components.CreatePostSection
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.components.PostItem
import com.codevalley.app.ui.viewmodel.NewsFeedViewModel

@Composable
fun NewsFeedScreen(navController: NavController, newsFeedViewModel: NewsFeedViewModel = hiltViewModel()) {
    val posts by PostStore.posts.collectAsState()
    val errorMessage by newsFeedViewModel.errorMessage.collectAsState()
    val isLoading by newsFeedViewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val userProfile = UserStore.getUserProfile()
    var postContent by remember { mutableStateOf("") }
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        fileUri = uri
    }

    LaunchedEffect(Unit) {
        newsFeedViewModel.loadMorePosts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News Feed") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                modifier = Modifier.height(56.dp),
                actions = {
                    IconButton(onClick = { navController.navigate("userSearch") }) {
                        Icon(Icons.Default.Search, contentDescription = "Search Users")
                    }
                    AvatarComponent(navController, userProfile)
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                CreatePostSection(
                    postContent = postContent,
                    onPostContentChange = { postContent = it },
                    onPostClick = {
                        newsFeedViewModel.createPost(context, CreatePostDto(postContent), fileUri)
                        postContent = ""
                    },
                    fileUri = fileUri,
                    onPickFileClick = { launcher.launch(arrayOf("*/*")) }
                )
                if (isLoading && posts.isEmpty()) {
                    LoadingIndicator()
                } else {
                    if (errorMessage.isNotEmpty()) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(posts) { post ->
                            PostItem(
                                post = post,
                                onLike = { newsFeedViewModel.likePost(post.id) },
                                onUnlike = { newsFeedViewModel.unlikePost(post.id) },
                                navController = navController,
                                viewModel = newsFeedViewModel
                            )
                        }
                        item {
                            if (isLoading) {
                                LoadingIndicator()
                            } else {
                                SideEffect {
                                    newsFeedViewModel.loadMorePosts()
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}