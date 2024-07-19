package com.codevalley.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codevalley.app.model.UserResponseDTO

@Composable
fun JoinRequestItem(
    user: UserResponseDTO,
    currentUserId: Int?,
    onProfileClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onRefuseClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = user.username, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = user.email, fontSize = 14.sp, color = Color.Gray)
        }
        Button(onClick = onProfileClick, modifier = Modifier.padding(end = 8.dp)) {
            Text("Profile")
        }
        Button(onClick = onAcceptClick, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green), modifier = Modifier.padding(end = 8.dp)) {
            Text("Accept", color = Color.White)
        }
        Button(onClick = onRefuseClick, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)) {
            Text("Refuse", color = Color.White)
        }
    }
}
