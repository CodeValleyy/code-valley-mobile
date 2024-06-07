package com.codevalley.app.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.model.CreatePostDto
import com.codevalley.app.ui.components.CreatePostSection
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.components.PostItem
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.NewsFeedViewModel
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception

@Composable
fun NewsFeedScreen(navController: NavController, newsFeedViewModel: NewsFeedViewModel = hiltViewModel()) {
    val posts by newsFeedViewModel.posts.collectAsState()
    val errorMessage by newsFeedViewModel.errorMessage.collectAsState()
    val isLoading by newsFeedViewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val userProfile = newsFeedViewModel.userProfile
    var searchQuery by remember { mutableStateOf("") }
    var postContent by remember { mutableStateOf("") }
    val imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { _: Uri? ->
       // imageUri = uri
    }

    LaunchedEffect(Unit) {
        newsFeedViewModel.loadPosts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .background(MaterialTheme.colors.surface, CircleShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search") },
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colors.onSurface,
                                backgroundColor = Color.Transparent,
                                cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.body2.fontSize)
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                modifier = Modifier.height(56.dp),
                actions = {
                    if (userProfile != null) {
                        IconButton(onClick = {
                            navController.navigate("${ScreenName.Profile}/${userProfile.id}")
                        }) {
                            Image(
                                painter = rememberAsyncImagePainter(userProfile.avatar),
                                contentDescription = "User Avatar",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                CreatePostSection(
                    postContent = postContent,
                    onPostContentChange = { postContent = it },
                    onPostClick = {
                        newsFeedViewModel.createPost(CreatePostDto(postContent))
                        postContent = ""
                    },
                    imageUri = imageUri,
                    onPickImageClick = { launcher.launch("image/*") }
                )
                if (isLoading) {
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
                    }
                }
            }
        }
    )
}
