package com.codevalley.app.ui.navigation

enum class ScreenName(private val value: String) {
    Main("main"),
    Login("login"),
    Register("Register"),
    NewsFeed("newsfeed"),
    Profile("profile"),
    Settings("settings"),
    PostDetail("postDetail"),
    Followers("followers"),
    Following("following"),
    Notification("notification");

    override fun toString(): String {
        return value
    }
}
