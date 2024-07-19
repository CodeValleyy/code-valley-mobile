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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codevalley.app.model.UserResponseDTO

@Composable
fun MemberItem(
    member: UserResponseDTO,
    currentUserId: Int?,
    currentUserIsAdmin: Boolean,
    memberIsAdmin: Boolean,
    onProfileClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onMakeAdminClick: () -> Unit
) {
    val canRemove = remember {
        currentUserIsAdmin && currentUserId != member.id && !memberIsAdmin
    }

    val canMakeAdmin = remember {
        currentUserIsAdmin && !memberIsAdmin
    }

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
            Text(text = member.username, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = member.email, fontSize = 14.sp, color = Color.Gray)
        }
        Button(onClick = onProfileClick, modifier = Modifier.padding(end = 8.dp)) {
            Text("Profile")
        }
        if (canRemove) {
            Button(onClick = onRemoveClick, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red), modifier = Modifier.padding(end = 8.dp)) {
                Text("Remove", color = Color.White)
            }
        }
        if (canMakeAdmin && !memberIsAdmin && currentUserId != member.id) {
            Button(onClick = onMakeAdminClick, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)) {
                Text("Make Admin", color = Color.White)
            }
        }
    }
}
