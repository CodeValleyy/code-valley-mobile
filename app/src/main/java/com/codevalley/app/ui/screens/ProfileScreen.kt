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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.codevalley.app.R
import com.codevalley.app.ui.components.LoadingIndicator
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(userId: Int, navController: NavController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val profileState by profileViewModel::profile
    val currentUser by profileViewModel::currentUser
    val errorMessage by profileViewModel::errorMessage
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            profileViewModel.uploadAvatar(userId, it)
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(userId)
    }

    BackHandler {
        navController.popBackStack()
    }

    if (profileState == null && errorMessage == null) {
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
    }
    else {
        profileState?.let { profile ->
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

                    if (currentUser?.id == userId) {
                        IconButton(
                            onClick = { navController.navigate(ScreenName.Settings.toString()) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        val painter = if (imageUri != null) {
                            rememberAsyncImagePainter(imageUri)
                        } else if (profile.avatar.isNotEmpty()) {
                            rememberAsyncImagePainter(profile.avatar)
                        } else {
                            painterResource(id = R.drawable.image)
                        }

                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.surface)
                                .clickable(enabled = currentUser?.id == userId) {
                                    imagePickerLauncher.launch("image/*")
                                },
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = profile.username,
                            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "@${profile.username}",
                            style = MaterialTheme.typography.body1,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = { /* TODO: Handle button click */ }) {
                                Text(text = "Message")
                            }
                            Button(onClick = { /* TODO: Handle button click */ }) {
                                Text(text = "Follow")
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ProfileStat(label = "Followers", value = "6.3k")
                            ProfileStat(label = "Posts", value = "572")
                            ProfileStat(label = "Following", value = "2.5k")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // TODO: static content, replace with actual data
                        Text("User posts here")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStat(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
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
        value = "6.3k"
    )
}
