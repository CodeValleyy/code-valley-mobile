package com.codevalley.app.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CreatePostSection(
    postContent: String,
    onPostContentChange: (String) -> Unit,
    onPostClick: () -> Unit,
    fileUri: Uri?,
    onPickFileClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Quoi de neuf ?!", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = postContent,
            onValueChange = onPostContentChange,
            placeholder = { Text("Écrire un post...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (fileUri != null) {
            Text(
                text = "Fichier sélectionné : ${fileUri.lastPathSegment}",
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.2f))
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onPickFileClick) {
                Icon(Icons.Default.Info, contentDescription = "Joindre un fichier")
            }
            Button(onClick = onPostClick) {
                Text(text = "POSTER")
            }
        }
    }
}
