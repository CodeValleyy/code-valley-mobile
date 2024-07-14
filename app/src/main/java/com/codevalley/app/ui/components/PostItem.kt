package com.codevalley.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.model.PostResponseDto
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.NewsFeedViewModel
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeThemeType
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PostItem(
    post: PostResponseDto,
    onLike: () -> Unit,
    onUnlike: () -> Unit,
    navController: NavController,
    viewModel: NewsFeedViewModel,
    isDetailScreen: Boolean = false
) {
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }
    val userProfile = viewModel.userProfile
    var localPost by remember { mutableStateOf(post) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(enabled = !isDetailScreen) { navController.navigate("${ScreenName.PostDetail}/${post.id}") }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(post.avatar ?: ""),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { navController.navigate("${ScreenName.Profile}/${post.userId}") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.clickable { navController.navigate("${ScreenName.Profile}/${post.userId}") }
                ) {
                    Text(localPost.username, fontWeight = FontWeight.Bold)
                    Text(dateFormat.format(localPost.createdAt), style = MaterialTheme.typography.body2)
                }
                Spacer(modifier = Modifier.weight(1f))
                if (userProfile?.id == localPost.userId) {
                    IconButton(onClick = {
                        viewModel.deletePost(localPost.id)
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Post")
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(localPost.content, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            if (!localPost.avatar.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter("data:image/png;base64,${localPost.avatar}"),
                    contentDescription = "Post Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (!localPost.code.isNullOrEmpty()) {
                val language = when (post.code_language?.lowercase(Locale.getDefault())) {
                    "java" -> CodeLang.Java
                    "python" -> CodeLang.Python
                    "javascript" -> CodeLang.JavaScript
                    "rust" -> CodeLang.Rust
                    "lua" -> CodeLang.Lua
                    else -> CodeLang.Python
                }
                val parser = remember { PrettifyParser() }
                val themeState by remember { mutableStateOf(CodeThemeType.Default) }
                val theme = remember(themeState) { themeState.theme() }

                val parsedCode = remember {
                    parseCodeAsAnnotatedString(
                        parser = parser,
                        theme = theme,
                        lang = language,
                        code = localPost.code!!
                    )
                }
                Text(
                    text = parsedCode,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    localPost = if (localPost.userHasLiked) {
                        onUnlike()
                        localPost.copy(userHasLiked = false, likes = localPost.likes - 1)
                    } else {
                        onLike()
                        localPost.copy(userHasLiked = true, likes = localPost.likes + 1)
                    }
                }) {
                    Icon(
                        imageVector = if (localPost.userHasLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = null
                    )
                }
                Text("${localPost.likes}")
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
