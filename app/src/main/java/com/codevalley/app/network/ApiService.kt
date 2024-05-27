package com.codevalley.app.network

import com.codevalley.app.model.DataModel
import retrofit2.http.GET

interface ApiService {
    @GET("endpoint")
    suspend fun getData(): List<DataModel>
}