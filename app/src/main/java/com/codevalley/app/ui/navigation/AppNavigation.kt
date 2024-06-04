package com.codevalley.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codevalley.app.ui.screens.LoginScreen
import com.codevalley.app.ui.screens.MainScreen
import com.codevalley.app.ui.screens.ProfileScreen
import com.codevalley.app.ui.screens.SettingsScreen
import com.codevalley.app.utils.Constants

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("profile") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 1
            ProfileScreen(
                userId = userId,
                navController = navController
            )
        }
        composable("settings") {
            SettingsScreen(
                navController = navController)
        }
    }
}
