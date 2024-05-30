package com.codevalley.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.rememberImagePainter
import com.codevalley.app.R
import com.codevalley.app.utils.Constants

@Composable
fun ProfileScreen(userId: Int, token: String, navController: NavController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val profileState by profileViewModel::profile
    val errorMessage by profileViewModel::errorMessage

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(userId, token)
    }

    BackHandler {
        navController.popBackStack()
    }

    if(profileState == null && errorMessage == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else

    if(errorMessage != null) {
        AlertDialog(
            onDismissRequest = { /* Do nothing */ },
            title = { Text(text = "Erreur") },
            text = { Text(text = errorMessage!!) },
            confirmButton = {
                Button(
                    onClick = {
                        profileViewModel.errorMessage = null
                        navController.navigate("main") {
                            popUpTo("profile") { inclusive = true }
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    } else {
        profileState?.let { profile ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Retour")
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    val painter = if (!profile.avatar.isNullOrEmpty()) {
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
                            .background(MaterialTheme.colors.surface),
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
                    // Your follow and message buttons
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
                    // Display user statistics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStat(label = "Followers", value = "6.3k")
                        ProfileStat(label = "Posts", value = "572")
                        ProfileStat(label = "Following", value = "2.5k")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Display user posts
                    // Example static content, replace with actual data
                    Text("User posts here")
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
        token = Constants.BEARER_TOKEN,
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
