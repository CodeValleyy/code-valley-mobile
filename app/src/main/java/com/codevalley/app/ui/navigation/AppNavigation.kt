package com.codevalley.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codevalley.app.ui.screens.LoginScreen
import com.codevalley.app.ui.screens.MainScreen
import com.codevalley.app.ui.screens.PostScreen
import com.codevalley.app.ui.screens.ProfileScreen
import com.codevalley.app.ui.screens.RegisterScreen
import com.codevalley.app.ui.screens.SettingsScreen
import com.codevalley.app.utils.Constants

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = ScreenName.Main.toString()) {
        composable(ScreenName.Main.toString()) {
            MainScreen(navController)
        }
        composable(ScreenName.Login.toString()) {
            LoginScreen(navController)
        }
        composable(ScreenName.Register.toString()) {
            RegisterScreen(navController)
        }
        composable(ScreenName.Profile.toString()) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 1
            ProfileScreen(
                userId = userId,
                navController = navController
            )
        }
        composable(ScreenName.Settings.toString()) {
            SettingsScreen(navController)
        }
        composable("post") {
            PostScreen(
                navController = navController,
                token = Constants.BEARER_TOKEN
            )
        }

    }
}
