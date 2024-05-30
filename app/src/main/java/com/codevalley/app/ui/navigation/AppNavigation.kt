package com.codevalley.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codevalley.app.ui.screens.MainScreen
import com.codevalley.app.ui.screens.ProfileScreen

@Composable
fun AppNavigation(token: String) {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable("profile") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
            val token = backStackEntry.arguments?.getString("token") ?: ""
            ProfileScreen(
                userId = userId,
                token = token,
                navController = navController
            )
        }
    }
}
