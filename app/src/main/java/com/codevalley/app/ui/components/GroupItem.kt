package com.codevalley.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.model.GroupResponseDTO

@Composable
fun GroupItem(
    group: GroupResponseDTO,
    currentUserId: Int?,
    onJoinClick: () -> Unit,
    onGroupClick: (Int) -> Unit
) {
    val isMember = group.members.any { it.id == currentUserId }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .clickable { if (isMember) onGroupClick(group.id) else onJoinClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(group.avatar),
            contentDescription = "Group Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = group.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            group.description?.let {
                Text(text = it, fontSize = 14.sp, color = Color.Gray)
            }
        }
        if (!isMember) {
            Button(
                onClick = onJoinClick,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(text = "Join")
            }
        }
    }
}


