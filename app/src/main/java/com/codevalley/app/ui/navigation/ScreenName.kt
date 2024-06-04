package com.codevalley.app.ui.navigation

enum class ScreenName(private val value: String) {
    Main("main"),
    Login("login"),
    Register("Register"),
    Profile("profile"),
    Settings("settings");

    override fun toString(): String {
        return value
    }
}