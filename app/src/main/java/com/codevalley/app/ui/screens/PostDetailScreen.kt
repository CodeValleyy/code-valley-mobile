package com.codevalley.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.codevalley.app.store.PostStore
import com.codevalley.app.store.UserStore
import com.codevalley.app.ui.components.CommentInputSection
import com.codevalley.app.ui.components.CommentItem
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.components.PostItem
import com.codevalley.app.ui.viewmodel.NewsFeedViewModel
import com.codevalley.app.ui.viewmodel.PostCommentViewModel

@Composable
fun PostDetailScreen(
    postId: Int,
    navController: NavController,
    newsFeedViewModel: NewsFeedViewModel = hiltViewModel(),
    postCommentViewModel: PostCommentViewModel = hiltViewModel()
) {

    val post = PostStore.getPostById(postId)
    var commentText by remember { mutableStateOf("") }
    val comments by postCommentViewModel.comments.collectAsState()
    val loading by postCommentViewModel.loading.collectAsState()
    val error by postCommentViewModel.error.collectAsState()
    val currentUser= UserStore.currentUser.collectAsState().value ?: UserStore.getUserProfile()

    LaunchedEffect(postId) {
        postCommentViewModel.loadComments(postId)
    }

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
                        onDeleteComment = {
                            postCommentViewModel.deleteComment(post.id, comment.id)
                        },
                        onReplyComment = { /* Handle reply comment */ },
                        currentUserId = currentUser?.id ?: 0
                    )
                }
                item {
                    CommentInputSection(commentText, onCommentTextChange = { commentText = it }, onPostComment = {
                        postCommentViewModel.addComment(post.id, commentText)
                        commentText = ""
                    })
                }
            }
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.padding(padding))
            }
            error?.let {
                Text(it, color = MaterialTheme.colors.error, modifier = Modifier.padding(padding))
            }
        }
    )
}
