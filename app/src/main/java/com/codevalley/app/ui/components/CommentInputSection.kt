package com.codevalley.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommentInputSection(commentText: String, onCommentTextChange: (String) -> Unit, onPostComment: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = commentText,
            onValueChange = onCommentTextChange,
            placeholder = { Text("Write a comment...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onPostComment, modifier = Modifier.align(Alignment.End)) {
            Text("Post")
        }
    }
}