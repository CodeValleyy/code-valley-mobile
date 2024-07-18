package com.codevalley.app.utils

import java.util.Locale

object PostUtils {
    fun getCodeLanguageFromUrl(codeUrl: String): String? {
        val extension = codeUrl.split('.').lastOrNull()?.split('?')?.get(0) ?: return null

        return when (extension.lowercase(Locale.ROOT)) {
            "py" -> "python"
            "js" -> "javascript"
            "rs" -> "rust"
            "lua" -> "lua"
            else -> "Unknown"
        }
    }
}