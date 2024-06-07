package com.codevalley.app

import android.app.Application
import com.codevalley.app.utils.TokenManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CodeValley : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}
