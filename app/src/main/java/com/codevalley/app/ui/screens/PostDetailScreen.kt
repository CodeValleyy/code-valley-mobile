package com.codevalley.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.model.Comment
import com.codevalley.app.store.PostStore
import com.codevalley.app.store.UserStore.userProfile
import com.codevalley.app.ui.components.CommentInputSection
import com.codevalley.app.ui.components.CommentSection
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.NewsFeedViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PostDetailScreen(postId: Int, navController: NavController, newsFeedViewModel: NewsFeedViewModel = hiltViewModel()) {
    val post = PostStore.getPostById(postId)
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }
    var commentText by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf(listOf<Comment>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post de ${post?.username ?: "Unknown"}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            if (post != null) {
                Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
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
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(post.content, style = MaterialTheme.typography.body1, modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter("data:image/png;base64,${post.avatar}"),
                        contentDescription = "Post Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Comments", style = MaterialTheme.typography.h6, modifier = Modifier.padding(horizontal = 16.dp))
                    CommentSection(comments, onLikeComment = { commentId ->
                        comments = comments.map {
                            if (it.id == commentId) it.copy(hasLiked = !it.hasLiked) else it
                        }
                    }, onReplyComment = {
                        // Handle reply action
                    })
                    CommentInputSection(commentText, onCommentTextChange = { commentText = it }, onPostComment = {
                        val newComment = Comment(
                            id = comments.size + 1,
                            avatar = post.avatar,
                            username = userProfile?.username ?: "Anonymous",
                            content = commentText,
                            createdAt = Date(),
                            hasLiked = false
                        )
                        comments = comments + newComment
                        commentText = ""
                    })
                }
            } else {
                Text("Post not found", modifier = Modifier.padding(16.dp))
            }
        }
    )
}

