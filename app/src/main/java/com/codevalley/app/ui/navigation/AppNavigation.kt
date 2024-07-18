package com.codevalley.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codevalley.app.ui.screens.CurrentUserScreen
import com.codevalley.app.ui.screens.FollowersScreen
import com.codevalley.app.ui.screens.FollowingScreen
import com.codevalley.app.ui.screens.PostDetailScreen
import com.codevalley.app.ui.screens.LoginScreen
import com.codevalley.app.ui.screens.MainScreen
import com.codevalley.app.ui.screens.NewsFeedScreen
import com.codevalley.app.ui.screens.NotificationScreen
import com.codevalley.app.ui.screens.ProfileScreen
import com.codevalley.app.ui.screens.RegisterScreen
import com.codevalley.app.ui.screens.SettingsScreen
import com.codevalley.app.ui.screens.UserSearchScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
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
        composable("${ScreenName.Profile}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 1
            ProfileScreen(
                userId = userId,
                navController = navController,
            )
        }
        composable(ScreenName.CurrentUser.toString()) {
            CurrentUserScreen(navController)
        }
        composable(ScreenName.Settings.toString()) {
            SettingsScreen(navController)
        }
        composable(ScreenName.NewsFeed.toString()) {
            NewsFeedScreen(navController)
        }
        composable(ScreenName.Notification.toString()) {
            NotificationScreen(navController)
        }
        composable(ScreenName.UserSearch.toString()) {
            UserSearchScreen(navController)
        }

        composable("${ScreenName.PostDetail}/{post.id}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("post.id")?.toIntOrNull()
            if (postId != null) {
                PostDetailScreen(postId, navController)
            } else {
                // Handle error
            }
        }

        composable("${ScreenName.Followers}/{profile.id}/{currentUser.id}") { backStackEntry ->
            val profileId = backStackEntry.arguments?.getString("profile.id")?.toInt() ?: 1
            val currentUserId = backStackEntry.arguments?.getString("currentUser.id")?.toInt() ?: 1
            FollowersScreen(
                navController = navController,
                userId = profileId,
                currentUserId = currentUserId
            )
        }

        composable("${ScreenName.Following}/{profile.id}/{currentUser.id}") { backStackEntry ->
            val profileId = backStackEntry.arguments?.getString("profile.id")?.toInt() ?: 1
            val currentUserId = backStackEntry.arguments?.getString("currentUser.id")?.toInt() ?: 1
            FollowingScreen(
                navController = navController,
                userId = profileId,
                currentUserId = currentUserId
            )
        }
    }
}
