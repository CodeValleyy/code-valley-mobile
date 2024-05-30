package com.codevalley.app.repository

import android.annotation.SuppressLint
import com.codevalley.app.model.UserResponseDTO
import com.codevalley.app.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import javax.inject.Inject
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException
import java.util.Date


class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val retrofit: Retrofit,
    private val context: android.content.Context
) {

    private fun createAuthorizedApiService(token: String): ApiService {
        val authInterceptor = okhttp3.Interceptor { chain ->
            val original = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Authorization", "Bearer $token")
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }

        val client: Retrofit = retrofit.newBuilder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build()
            )
            .build()

        return client.create(ApiService::class.java)
    }

    suspend fun getProfile(id: Int, token: String): UserResponseDTO {
        val authorizedApiService = createAuthorizedApiService(token)
        return authorizedApiService.getProfile(id)
    }

    @SuppressLint("Recycle")
    @Throws(IOException::class)
    suspend fun uploadAvatar(userId: Int, fileUri: Uri, token: String): String {
        val authorizedApiService = createAuthorizedApiService(token)

        val userIdRequestBody = userId.toString().toRequestBody(MultipartBody.FORM)
        val contentResolver = context.contentResolver

        val mimeType = contentResolver.getType(fileUri)
        val inputStream = contentResolver.openInputStream(fileUri)
            ?: throw IOException("Unable to open input stream for URI: $fileUri")
        val fileBytes = inputStream.readBytes()
        val fileRequestBody = fileBytes.toRequestBody(mimeType?.toMediaTypeOrNull())
        val filePart =
            MultipartBody.Part.createFormData("file", "avatar-${Date().time}.jpg", fileRequestBody)

        return try {
            val response = authorizedApiService.uploadAvatar(userIdRequestBody, filePart)
            response.avatarUrl
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            throw IOException("HTTP ${e.code()} ${e.message()}: $errorBody")
        }
    }
}