package com.example.gitusersassignment.usersearchscreen.di

import com.example.gitusersassignment.usersearchscreen.api.GithubUsersAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GithubUserAPIModule {

    @Provides
    @Singleton
    fun getGithubUsersAPI(retrofit: Retrofit): GithubUsersAPI =
        retrofit.create(GithubUsersAPI::class.java)
}