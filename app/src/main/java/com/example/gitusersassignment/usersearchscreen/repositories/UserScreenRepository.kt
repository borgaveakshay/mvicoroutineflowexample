package com.example.gitusersassignment.usersearchscreen.repositories

import com.example.gitusersassignment.base.Result
import com.example.gitusersassignment.usersearchscreen.datamodels.UserDetailViewModel
import com.example.gitusersassignment.usersearchscreen.datamodels.UserViewModel
import kotlinx.coroutines.flow.Flow

interface UserScreenRepository {
    suspend fun getGithubUsers(): Flow<Result<List<UserViewModel>>>
    suspend fun getGithubUserDetails(userName: String): Flow<Result<UserDetailViewModel>>
}