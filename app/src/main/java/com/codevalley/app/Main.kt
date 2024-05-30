package com.codevalley.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.codevalley.app.ui.theme.CodeValleyTheme
import com.codevalley.app.ui.navigation.AppNavigation
import com.codevalley.app.ui.screens.ProfileScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodeValleyTheme {
                AppNavigation(token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZW1haWwiOiJyaWNhcmRvLmp1ZXpAZ21haWwuY29tIiwidXNlcm5hbWUiOiJjYXJsaXRvMDYwNSIsImxhc3RMb2dpbkF0IjoiMjAyNC0wNS0yOVQxODo0NDozOC45ODRaIiwiY3JlYXRlZEF0IjoiMjAyNC0wNS0yOVQxODo0NDozOC45ODRaIiwiaXNUd29GYWN0b3JBdXRoZW50aWNhdGVkIjpmYWxzZSwiaWF0IjoxNzE3MDA4Mjg4LCJleHAiOjE3MTcwOTQ2ODh9.6Xwkbe4a1QK6YsvVZES3-CIy0grpmxQL625fBVswqfU")
            }
        }
    }
}
