package com.codevalley.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.codevalley.app.ui.theme.CodeValleyTheme
import com.codevalley.app.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodeValleyTheme {
                AppNavigation()
            }
        }
    }
}
