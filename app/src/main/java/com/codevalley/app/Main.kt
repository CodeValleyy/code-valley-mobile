package com.codevalley.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.codevalley.app.ui.navigation.AppNavigation
import com.codevalley.app.ui.navigation.ScreenName
import com.codevalley.app.ui.theme.CodeValleyTheme
import com.codevalley.app.utils.TokenManager
import com.codevalley.app.store.UserStore
import com.codevalley.app.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodeValleyTheme {
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    scope.launch {
                        if (!TokenManager.token.isNullOrEmpty()) {
                            try {
                                UserStore.setUserProfile(userRepository.getMe())
                                navController.navigate(ScreenName.NewsFeed.toString()) {
                                    popUpTo(ScreenName.Main.toString()) { inclusive = true }
                                }
                            } catch (e: Exception) {
                                TokenManager.clearToken()
                            }
                        }
                    }
                }
                AppNavigation(navController)
            }
        }
    }
}
