package com.codevalley.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.store.UserStore
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.utils.Constants

@Composable
fun AvatarComponent(navController: NavController, userProfile: UserResponseDTO?) {
    val user = UserStore.getUserProfile()
    val avatar = if (userProfile?.avatar.isNullOrEmpty()) {
        Constants.DEFAULT_AVATAR_URL
    } else {
        userProfile?.avatar!!
    }

    IconButton(onClick = {
        if (userProfile?.id == user?.id) {
            navController.navigate(ScreenName.CurrentUser.toString())
        }
        else {
            navController.navigate(ScreenName.Profile.toString() + "/${userProfile?.id}")
        }
    }) {
        Image(
            painter = rememberAsyncImagePainter(avatar),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
    }
}
