package com.example.gitusersassignment.usersearchscreen.datamodels

import android.os.Parcelable
import com.example.gitusersassignment.base.Result
import com.example.gitusersassignment.base.ResultStatus
import com.example.gitusersassignment.usersearchscreen.contracts.UserScreenContract
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailViewModel(
    val avatarUrl: String? = "",
    val bio: String? = null,
    val blog: String?,
    val company: String? = "",
    val email: String? = null,
    val followers: Int?,
    val following: Int?,
    val location: String?,
    val name: String? = "",
    val publicRepos: Int?,
    val twitterUsername: String?,
    val login : String?

) : Parcelable

fun Result<UserDetailViewModel>.toState(): UserScreenContract.GetUserDetailViewState {
    return when (this.status) {
        ResultStatus.Error -> UserScreenContract.GetUserDetailViewState.Error(this.error!!)
        ResultStatus.Loading -> UserScreenContract.GetUserDetailViewState.Loading
        ResultStatus.Success -> UserScreenContract.GetUserDetailViewState.Success(this.data)
    }
}