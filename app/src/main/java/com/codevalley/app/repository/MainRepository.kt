package com.codevalley.app.repository

import com.codevalley.app.network.ApiService

class MainRepository(private val apiService: ApiService) {
    suspend fun getData() = apiService.getData()
}