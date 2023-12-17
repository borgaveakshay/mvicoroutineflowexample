package com.example.gitusersassignment.usersearchscreen.repositories.impl

import com.example.gitusersassignment.base.Result
import com.example.gitusersassignment.usersearchscreen.api.GithubUsersAPI
import com.example.gitusersassignment.usersearchscreen.datamodels.UserDetailViewModel
import com.example.gitusersassignment.usersearchscreen.datamodels.UserViewModel
import com.example.gitusersassignment.usersearchscreen.repositories.UserScreenRepository
import com.example.gitusersassignment.usersearchscreen.responsemodel.userdetail.toDetailViewModel
import com.example.gitusersassignment.usersearchscreen.responsemodel.users.toViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class UserScreenProcessor @Inject constructor(private val githubUsersAPI: GithubUsersAPI) :
    UserScreenRepository {
    override suspend fun getGithubUsers(): Flow<Result<List<UserViewModel>>> =
        flow {
            emit(
                Result.success(
                    githubUsersAPI.getGithubUsers().toViewModel()
                )
            )
        }.catch {
            emit(Result.error(it))
        }.onStart {
            emit(Result.loading())
        }

    override suspend fun getGithubUserDetails(userName: String): Flow<Result<UserDetailViewModel>> =
        flow {
            emit(Result.success(githubUsersAPI.getGithubUserDetails(userName).toDetailViewModel()))
        }.catch {
            emit(Result.error(it))
        }.onStart {
            emit(Result.loading())
        }
}