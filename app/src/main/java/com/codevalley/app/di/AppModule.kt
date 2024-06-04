package com.codevalley.app.di

import android.app.Application
import android.content.Context
import com.codevalley.app.network.AuthService
import com.codevalley.app.network.FriendshipService
import com.codevalley.app.network.GroupService
import com.codevalley.app.repository.FriendshipRepository
import com.codevalley.app.repository.GroupRepository
import com.codevalley.app.repository.UserRepository
import com.codevalley.app.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(
                Constants.BASE_URL
            )
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideFriendshipService(retrofit: Retrofit): FriendshipService {
        return retrofit.create(FriendshipService::class.java)
    }

    @Provides
    @Singleton
    fun provideGroupService(retrofit: Retrofit): GroupService {
        return retrofit.create(GroupService::class.java)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideUserRepository(retrofit: Retrofit, context: Context): UserRepository {
        return UserRepository(retrofit, context)
    }

    @Provides
    @Singleton
    fun provideFriendshipRepository(retrofit: Retrofit): FriendshipRepository {
        return FriendshipRepository(retrofit)
    }

    @Provides
    @Singleton
    fun provideGroupRepository(retrofit: Retrofit): GroupRepository {
        return GroupRepository(retrofit)
    }

}
