package com.codevalley.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.codevalley.app.ui.components.CommentItem
import com.codevalley.app.ui.components.CommentSection
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.components.PostItem
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.NewsFeedViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PostDetailScreen(postId: Int, navController: NavController, newsFeedViewModel: NewsFeedViewModel = hiltViewModel()) {
    val post = PostStore.getPostById(postId)
    var commentText by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf(listOf<Comment>()) }

    if (post == null) {
        LoadingIndicator()
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post de ${post.username}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                item {
                    PostItem(
                        post = post,
                        onLike = { newsFeedViewModel.likePost(post.id) },
                        onUnlike = { newsFeedViewModel.unlikePost(post.id) },
                        navController = navController,
                        viewModel = newsFeedViewModel,
                        isDetailScreen = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Comments", style = MaterialTheme.typography.h6, modifier = Modifier.padding(horizontal = 16.dp))
                }
                items(comments) { comment ->
                    CommentItem(
                        comment = comment,
                        onLikeComment = {
                            comments = comments.map {
                                if (it.id == comment.id) it.copy(hasLiked = !it.hasLiked) else it
                            }
                        },
                        onReplyComment = {
                            // Handle reply action
                        }
                    )
                }
                item {
                    CommentInputSection(commentText, onCommentTextChange = { commentText = it }, onPostComment = {
                        val newComment = Comment(
                            id = comments.size + 1,
                            avatar = post.avatar ?: "",
                            username = userProfile?.username ?: "Anonymous",
                            content = commentText,
                            userId = userProfile?.id ?: 0,
                            createdAt = Date(),
                            hasLiked = false,
                        )
                        comments = comments + newComment
                        commentText = ""
                    })
                }
            }
        }
    )
}
