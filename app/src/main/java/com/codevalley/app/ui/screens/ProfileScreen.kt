package com.codevalley.app.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.model.PostResponseDto
import com.codevalley.app.store.FriendshipStore
import com.codevalley.app.store.PostStore
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.ProfileViewModel
import com.codevalley.app.utils.Constants

@Composable
fun ProfileScreen(userId: Int, navController: NavController,
                  profileViewModel: ProfileViewModel = hiltViewModel()) {
    val profileState by profileViewModel::profile
    val currentUser by profileViewModel.currentUser.collectAsState()
    val isLoading by profileViewModel::isLoading
    val errorMessage by profileViewModel::errorMessage
    val numberOfPostsByUserId = PostStore.getNumberOfPostsByUserId(userId)
    var numberOfFollowers by remember { mutableIntStateOf(0) }
    var numberOfFollowing by remember { mutableIntStateOf(0) }
    val countFollowersAndFollowing by FriendshipStore.followersAndFollowingsCount.collectAsState()

    val userPosts by remember { mutableStateOf(PostStore.getPostsByUserId(userId)) }
    val isFollowing by profileViewModel::isFollowing

    val avatarUrl = if (profileState?.avatar.isNullOrEmpty()) {
        Constants.DEFAULT_AVATAR_URL
    } else {
        profileState?.avatar!!
    }

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(userId)
        profileViewModel.countFollowersAndFollowing(userId)
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

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.surface),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${profileState?.username}",
                        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "@${profileState?.username}",
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
                                        profileViewModel.unfollowUser(userId)
                                    } else {
                                        profileViewModel.followUser(userId)
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
                            navController.navigate(ScreenName.Followers.toString() + "/${profileState?.id}/${currentUser?.id}") {
                                popUpTo(ScreenName.Profile.toString()) { inclusive = true }
                            }
                        })
                        ProfileStat(label = "Posts", value = numberOfPostsByUserId.toString(), onClick = {
                            /* TODO: Handle click */
                        })
                        ProfileStat(label = "Following", value = numberOfFollowing.toString(), onClick = {
                            navController.navigate(ScreenName.Following.toString() + "/${profileState?.id}/${currentUser?.id}") {
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

@Composable
fun ProfileStat(label: String, value: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
    }
}

@Composable
fun PostItem(post: PostResponseDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.username, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = post.content)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Likes: ${post.likes}", style = MaterialTheme.typography.body2)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileScreen(
        userId = 1,
        navController = navController
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileStatPreview() {
    ProfileStat(
        label = "Followers",
        value = "6.3k",
        onClick = { /* TODO: Handle click */ }
    )
}
