package com.codevalley.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.model.Comment
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CommentItem(comment: Comment, onLikeComment: () -> Unit, onReplyComment: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }

    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(comment.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(comment.username, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(dateFormat.format(comment.createdAt), style = MaterialTheme.typography.body2)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(comment.content, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onLikeComment) {
                    Icon(
                        imageVector = if (comment.hasLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = onReplyComment) {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Reply"
                    )
                }
            }
        }
    }
}
