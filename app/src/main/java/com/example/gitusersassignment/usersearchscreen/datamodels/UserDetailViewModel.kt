package com.example.gitusersassignment.usersearchscreen.datamodels

import com.example.gitusersassignment.base.Result
import com.example.gitusersassignment.base.ResultStatus
import com.example.gitusersassignment.usersearchscreen.contracts.UserScreenContract

data class UserDetailViewModel(
    val avatarUrl: String = "",
    val bio: String? = null,
    val blog: String?,
    val company: String = "",
    val email: String? = null,
    val followers: Int?,
    val following: Int?,
    val location: String?,
    val name: String = "",
    val publicRepos: Int?,
    val twitterUsername: String?

)

fun Result<UserDetailViewModel>.toState(): UserScreenContract.GetUserDetailViewState {
    return when (this.status) {
        ResultStatus.Error -> UserScreenContract.GetUserDetailViewState.Error(this.error!!)
        ResultStatus.Loading -> UserScreenContract.GetUserDetailViewState.Loading
        ResultStatus.Success -> UserScreenContract.GetUserDetailViewState.Success(this.data)
    }
}