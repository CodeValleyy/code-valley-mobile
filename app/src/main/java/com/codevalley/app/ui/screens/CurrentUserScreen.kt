package com.codevalley.app.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.store.FriendshipStore
import com.codevalley.app.store.PostStore
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.ProfileViewModel
import com.codevalley.app.utils.Constants

@Composable
fun CurrentUserScreen(navController: NavController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val currentUser by profileViewModel.currentUser.collectAsState()
    val isLoading by profileViewModel::isLoading
    val errorMessage by profileViewModel::errorMessage
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val numberOfPostsByUserId = PostStore.getNumberOfPostsByUserId(currentUser!!.id)
    var numberOfFollowers by remember { mutableIntStateOf(0) }
    var numberOfFollowing by remember { mutableIntStateOf(0) }
    val countFollowersAndFollowing by FriendshipStore.followersAndFollowingsCount.collectAsState()

    val userPosts by remember { mutableStateOf(PostStore.getPostsByUserId(currentUser!!.id)) }
    val isFollowing by profileViewModel::isFollowing

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            profileViewModel.uploadAvatar(currentUser!!.id, it)
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(currentUser!!.id)
        profileViewModel.countFollowersAndFollowing(currentUser!!.id)
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
                        profileViewModel.errorMessage = null
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
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = {
                        if (!navController.popBackStack()) {
                            navController.navigate(ScreenName.Main.toString())
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                IconButton(
                    onClick = { navController.navigate(ScreenName.Settings.toString()) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val avatarUrl = if (currentUser?.avatar.isNullOrEmpty()) {
                        Constants.DEFAULT_AVATAR_URL
                    } else {
                        "${currentUser?.username}"
                    }
                    val painter = rememberAsyncImagePainter(avatarUrl)

                    Image(
                        painter = painter,
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
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(onClick = {
                                    if (isFollowing) {
                                        profileViewModel.unfollowUser(currentUser!!.id)
                                    } else {
                                        profileViewModel.followUser(currentUser!!.id)
                                    }
                                }) {
                                    Text(text = if (isFollowing) "Unfollow" else "Follow")
                                }
                            }
                        }
                    }
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("User posts here")
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(userPosts) { post ->
                            PostItem(post = post, onClick = {
                                navController.navigate("${ScreenName.PostDetail}/${post.id}")
                            })
                        }
                    }
                }
            }
        }
    }
}
