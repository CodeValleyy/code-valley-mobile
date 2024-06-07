package com.codevalley.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.model.PostResponseDto
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.NewsFeedViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PostScreen(navController: NavController, newsFeedViewModel: NewsFeedViewModel = hiltViewModel()) {
    val posts by newsFeedViewModel.posts.collectAsState()
    val errorMessage by newsFeedViewModel.errorMessage.collectAsState()
    val isLoading by newsFeedViewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val userProfile = newsFeedViewModel.userProfile
    var searchQuery by remember { mutableStateOf("") }

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
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
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
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun PostItem(post: PostResponseDto, onLike: () -> Unit, onUnlike: () -> Unit, navController: NavController) {
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(post.avatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { navController.navigate("${ScreenName.Profile}/${post.userId}") }

                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(post.username, fontWeight = FontWeight.Bold)
                    Text(dateFormat.format(post.createdAt), style = MaterialTheme.typography.body2)
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { /* Follow user */ }) {
                    Text("Following")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(post.content, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter("data:image/png;base64,${post.avatar}"),
                contentDescription = "Post Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Voici mon script python qui me permet de donner les chiffres au carré")
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = if (post.userHasLiked) onUnlike else onLike) {
                    Icon(
                        imageVector = if (post.userHasLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = null
                    )
                }
                Text("${post.likes}")
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { /* Comment on post */ }) {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Comment on post"
                    )
                }
                Text("12")
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { /* Share post */ }) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share post"
                    )
                }
                Text("36")
            }
        }
    }
}
