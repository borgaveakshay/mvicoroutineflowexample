package com.example.gitusersassignment.usersearchscreen.api

import com.example.gitusersassignment.usersearchscreen.responsemodel.userdetail.GetUserDetailResponse
import com.example.gitusersassignment.usersearchscreen.responsemodel.users.GetUsersResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubUsersAPI {

    @GET("/users")
    suspend fun getGithubUsers(): GetUsersResponse

    @GET("/users/{userName}")
    suspend fun getGithubUserDetails(@Path("userName") userName: String): GetUserDetailResponse

}