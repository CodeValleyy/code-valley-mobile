package com.codevalley.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codevalley.app.model.Comment

@Composable
fun CommentSection(comments: List<Comment>, onLikeComment: (Int) -> Unit, onReplyComment: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(comments) { comment ->
            CommentItem(comment, onLikeComment = { onLikeComment(comment.id) }, onReplyComment = { onReplyComment(comment.id) })
        }
    }
}
