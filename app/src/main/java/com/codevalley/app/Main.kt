package com.codevalley.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.codevalley.app.ui.theme.CodeValleyTheme
import com.codevalley.app.ui.navigation.AppNavigation
import com.codevalley.app.utils.Constants.BEARER_TOKEN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodeValleyTheme {
                AppNavigation(token = BEARER_TOKEN)
            }
        }
    }
}
