package com.codevalley.app.ui.navigation

enum class ScreenName(private val value: String) {
    Main("main"),
    Login("login"),
    Register("Register"),
    NewsFeed("newsfeed"),
    Profile("profile"),
    CurrentUser("currentUser"),
    Settings("settings"),
    PostDetail("postDetail"),
    Followers("followers"),
    Following("following"),
    UserSearch("userSearch"),
    Notification("notification"),
    Groups("groups"),
    Messages("messages"),
    JoinRequests("joinRequests"),
    EditGroup("editGroup"),
    GroupMembers("groupMembers");

    override fun toString(): String {
        return value
    }
}
