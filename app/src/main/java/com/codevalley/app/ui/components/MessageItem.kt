package com.codevalley.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codevalley.app.model.MessageResponseDTO
import com.codevalley.app.store.UserStore

@Composable
fun MessageItem(message: MessageResponseDTO) {
    val currentUser = UserStore.currentUser.collectAsState()
    var authorOrYou = if (currentUser.value?.id == message.author.id) "You" else message.author.username
    var color = if (currentUser.value?.id == message.author.id) Color.Blue else Color.Black

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.LightGray)
        .padding(8.dp)) {
        Text(text = authorOrYou
            , fontWeight = FontWeight.Bold
            , color = color
        )
        Text(text = message.value, fontSize = 16.sp)
        Text(text = message.createdAt.toString(), fontSize = 12.sp, color = Color.Gray)
    }
}