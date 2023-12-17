package com.example.gitusersassignment.usersearchscreen.responsemodel.users


import com.example.gitusersassignment.usersearchscreen.datamodels.UserViewModel

class GetUsersResponse : ArrayList<GetUsersResponseItem>()

fun GetUsersResponse.toViewModel(): List<UserViewModel> = buildList {
    this@toViewModel.map { responseItem ->
        add(UserViewModel(userName = responseItem.login, avatarUrl = responseItem.avatarUrl))
    }
}