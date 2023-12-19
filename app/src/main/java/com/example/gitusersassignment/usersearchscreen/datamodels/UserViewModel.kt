package com.example.gitusersassignment.usersearchscreen.datamodels

import com.example.gitusersassignment.base.Result
import com.example.gitusersassignment.base.ResultStatus
import com.example.gitusersassignment.usersearchscreen.contracts.UserScreenContract

data class UserViewModel(val userName: String, val avatarUrl: String?)

fun Result<List<UserViewModel>>.toState(): UserScreenContract.GetUsersViewState {
    return when (this.status) {
        ResultStatus.Error -> UserScreenContract.GetUsersViewState.Error(this.error!!)
        ResultStatus.Loading -> UserScreenContract.GetUsersViewState.Loading
        ResultStatus.Success -> UserScreenContract.GetUsersViewState.Success(this.data)
    }
}