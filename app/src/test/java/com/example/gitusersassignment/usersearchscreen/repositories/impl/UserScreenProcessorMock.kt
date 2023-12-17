package com.example.gitusersassignment.usersearchscreen.repositories.impl

import com.example.gitusersassignment.usersearchscreen.datamodels.UserDetailViewModel
import com.example.gitusersassignment.usersearchscreen.datamodels.UserViewModel
import com.example.gitusersassignment.usersearchscreen.responsemodel.userdetail.GetUserDetailResponse
import com.example.gitusersassignment.usersearchscreen.responsemodel.users.GetUsersResponse
import com.example.gitusersassignment.usersearchscreen.responsemodel.users.GetUsersResponseItem

object UserScreenProcessorMock {

    fun getMockedGithubUsersResponse(): GetUsersResponse {
        val response = GetUsersResponse()
        for (i in 0..5) {
            response.add(
                GetUsersResponseItem(
                    avatarUrl = "https://avatarurl$i.com",

                    eventsUrl = "https://eventurl$i.com",

                    followersUrl = "https://followersurl$i.com",

                    followingUrl = "https://followingurl$i.com",

                    gistsUrl = "https://gistsurl$i.com",

                    gravatarId = "$i",

                    htmlUrl = "https://htmlurl$i.com",

                    id = i,

                    login = "login user $i",

                    nodeId = "$i",

                    organizationsUrl = "https://organizationurl$i.com",

                    receivedEventsUrl = "https://receivedeventsurl$i.com",

                    reposUrl = "https://reposurl$i.com",

                    siteAdmin = true,

                    starredUrl = "https://starredurl$i.com",

                    subscriptionsUrl = "https://subscriptionsurl$i.com",

                    type = "user",

                    url = "https://userurl$i.com"
                )
            )
        }
        return response
    }

    fun getUserViewModels(): List<UserViewModel> = buildList {
        for (i in 0..5) {
            add(
                UserViewModel(
                    userName = "login user $i",
                    avatarUrl = "https://avatarurl$i.com"
                )
            )
        }
    }

    fun getUserDetailResponse(): GetUserDetailResponse = GetUserDetailResponse(
        avatarUrl = "https://avatarurl.com",
        eventsUrl = "https://eventurl.com",
        followersUrl = "https://followersurl.com",
        followingUrl = "https://followingurl.com",
        gistsUrl = "https://gistsurl.com",
        gravatarId = "0",
        htmlUrl = "https://htmlurl.com",
        id = 5,
        login = "login user 0",
        nodeId = "10",
        organizationsUrl = "https://organizationurl.com",
        receivedEventsUrl = "https://receivedeventsurl.com",
        reposUrl = "https://reposurl.com",
        siteAdmin = true,
        starredUrl = "https://starredurl.com",
        subscriptionsUrl = "https://subscriptionsurl.com",
        type = "user",
        url = "https://userurl.com",
        bio = null,
        blog = "https://loginuser0blog.com",
        company = "Google Inc",
        createdAt = "2007-10-20T05:24:19Z",
        updatedAt = "2023-09-18T18:49:27Z",
        email = "abc@abc.com",
        followers = 100,
        following = 10,
        hireable = "No",
        location = "India",
        name = "Login user 0",
        publicRepos = 100,
        publicGists = 0,
        twitterUsername = "loginuser0"
    )

    fun getUserDetailModel(): UserDetailViewModel = UserDetailViewModel(
        avatarUrl = "https://avatarurl.com",
        bio = null,
        blog = "https://loginuser0blog.com",
        company = "Google Inc",
        email = "abc@abc.com",
        followers = 100,
        following = 10,
        location = "India",
        name = "Login user 0",
        publicRepos = 100,
        twitterUsername = "loginuser0"
    )

}