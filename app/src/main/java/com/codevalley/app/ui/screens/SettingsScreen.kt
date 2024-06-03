package com.codevalley.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.codevalley.app.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SettingsScreen(token: String, navController: NavController, settingsViewModel: SettingsViewModel = hiltViewModel()) {
    val isTwoFactorEnabled by settingsViewModel.isTwoFactorEnabled.collectAsState()
    val qrCodeUrl by settingsViewModel.qrCodeUrl.collectAsState()
    val setupKey by settingsViewModel.setupKey.collectAsState()
    val errorMessage by settingsViewModel.errorMessage.collectAsState()


    LaunchedEffect(Unit) {
        settingsViewModel.loadTwoFactorStatus(token)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Account Settings", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Two-Factor Authentication")
                        Switch(
                            checked = isTwoFactorEnabled,
                            onCheckedChange = { checked ->
                                if (checked) {
                                    settingsViewModel.enableTwoFactor(token)
                                } else {
                                    settingsViewModel.disableTwoFactor(token)
                                }
                            }
                        )
                    }

                    if (isTwoFactorEnabled && qrCodeUrl.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(" Enter this code in your app to enable two-factor authentication:")
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(setupKey, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.body1, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(8.dp))
                        CopyToClipboardButton(setupKey)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { /* TODO: Reset Password */ }) {
                        Text("Reset Password")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { /* TODO: Reset Email */ }) {
                        Text("Reset Email")
                    }
                }

                Button(
                    onClick = { settingsViewModel.logout() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log Out", color = MaterialTheme.colors.onError)
                }

                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colors.error)
                }
            }
        }
    )
}

@Composable
fun CopyToClipboardButton(setupKey: String) {
    val context = LocalContext.current
    Button(onClick = {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("2FA Code", setupKey)
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }) {
        Text("Copy to Clipboard")
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(Constants.BEARER_TOKEN, rememberNavController())
}
