package com.codevalley.app.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.store.FriendshipStore
import com.codevalley.app.store.PostStore
import com.codevalley.app.store.UserStore
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.CurrentUserViewModel
import com.codevalley.app.utils.Constants

@Composable
fun CurrentUserScreen(navController: NavController, currentUserViewModel: CurrentUserViewModel = hiltViewModel()) {
    val currentUser by UserStore.currentUser.collectAsState()
    val isLoading by currentUserViewModel::isLoading
    val errorMessage by currentUserViewModel::errorMessage
    val numberOfPostsByUserId = PostStore.getNumberOfPostsByUserId(currentUser!!.id)
    var numberOfFollowers by remember { mutableIntStateOf(0) }
    var numberOfFollowing by remember { mutableIntStateOf(0) }
    val countFollowersAndFollowing by FriendshipStore.followersAndFollowingsCount.collectAsState()
    val countNotification by currentUserViewModel::countNotification

    val avatar = if (currentUser?.avatar.isNullOrEmpty()) {
        Constants.DEFAULT_AVATAR_URL
    } else {
        currentUser?.avatar!!
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            currentUserViewModel.uploadAvatar(currentUser!!.id, it)
        }
    }

    LaunchedEffect(Unit) {
        currentUserViewModel.loadProfile(currentUser!!.id)
        currentUserViewModel.countFollowersAndFollowing(currentUser!!.id)
        currentUserViewModel.countUnreadNotifications()
    }

    LaunchedEffect(countFollowersAndFollowing) {
        countFollowersAndFollowing?.let {
            numberOfFollowers = it.followers
            numberOfFollowing = it.followings
        }
    }

    BackHandler {
        navController.popBackStack()
    }


    if (isLoading) {
        LoadingIndicator()
    } else if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { /* Do nothing */ },
            title = { Text(text = "Erreur") },
            text = { Text(text = errorMessage!!) },
            confirmButton = {
                Button(
                    onClick = {
                        currentUserViewModel.errorMessage = null
                        navController.navigate(ScreenName.Main.toString()) {
                            popUpTo(ScreenName.Profile.toString()) { inclusive = true }
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate(ScreenName.Main.toString())
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                IconButton(
                    onClick = { navController.navigate(ScreenName.Settings.toString()) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(avatar),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.surface)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${currentUser?.username}",
                        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "@${currentUser?.username}",
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStat(label = "Followers", value = numberOfFollowers.toString(), onClick = {
                            navController.navigate(ScreenName.Followers.toString() + "/${currentUser?.id}/${currentUser?.id}") {
                                popUpTo(ScreenName.Profile.toString()) { inclusive = true }
                            }
                        })
                        ProfileStat(label = "Posts", value = numberOfPostsByUserId.toString(), onClick = {
                            /* TODO: Handle click */
                        })
                        ProfileStat(label = "Following", value = numberOfFollowing.toString(), onClick = {
                            navController.navigate(ScreenName.Following.toString() + "/${currentUser?.id}/${currentUser?.id}") {
                                popUpTo(ScreenName.Profile.toString()) { inclusive = true }
                            }
                        })
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Column {
                        DividerRow()
                        OptionItem("Notification", countNotification, onClick = {
                            navController.navigate(ScreenName.Notification.toString())
                        })
                        DividerRow()
                        OptionItem(label = "Friends", onClick = {
                            navController.navigate(ScreenName.Followers.toString() + "/${currentUser?.id}/${currentUser?.id}") {
                                popUpTo(ScreenName.Profile.toString()) { inclusive = true }
                            }
                        })
                        DividerRow()
                        OptionItem(label = "Messages")
                        DividerRow()
                        OptionItem(label = "Groups", onClick = {
                            navController.navigate(ScreenName.Groups.toString())
                        })
                        DividerRow()
                        OptionItem(label = "Favorites")
                        DividerRow()
                        OptionItem(label = "Log out", onClick = {
                            currentUserViewModel.logout()
                            navController.navigate(ScreenName.Login.toString())
                        })
                        DividerRow()
                    }
                }
            }
        }
    }
}

@Composable
fun OptionItem(label: String, count: Int = 0, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 18.sp, modifier = Modifier.weight(1f))
        if (count != 0) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFF6200EE), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = count.toString(), color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun DividerRow() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentUserPreview() {
    CurrentUserScreen(rememberNavController())
}
