package com.example.gitusersassignment.usersearchscreen.contracts

import com.example.gitusersassignment.base.mvi.UiEvent
import com.example.gitusersassignment.base.mvi.UiState
import com.example.gitusersassignment.usersearchscreen.datamodels.UserDetailViewModel
import com.example.gitusersassignment.usersearchscreen.datamodels.UserViewModel

class UserScreenContract {

    /**
     * UI events for the user screen
     */
    sealed class UserScreenEvent : UiEvent {
        data object GetUsersList : UserScreenEvent()
        data class GetUserDetails(val name: String) : UserScreenEvent()
    }

    /**
     * View state related to get users from github api
     */
    sealed class GetUsersViewState {
        data object Idle : GetUsersViewState()
        data object Loading : GetUsersViewState()
        data class Success(val userViewModel: List<UserViewModel>? = null) : GetUsersViewState()
        data class Error(val exception: Throwable? = null) : GetUsersViewState()
    }

    /**
     * View state for Github user detail api
     */
    sealed class GetUserDetailViewState {
        data object Idle : GetUserDetailViewState()
        data object Loading : GetUserDetailViewState()
        data class Success(val userDetailViewModel: UserDetailViewModel? = null) :
            GetUserDetailViewState()

        data class Error(val exception: Throwable? = null) : GetUserDetailViewState()
    }

    /**
     * View state for user screen with initial state
     */
    data class UserScreenState(
        val usersViewState: GetUsersViewState,
        val userDetailViewState: GetUserDetailViewState
    ) : UiState {

        companion object {
            val initialState = UserScreenState(
                usersViewState = GetUsersViewState.Idle,
                userDetailViewState = GetUserDetailViewState.Idle
            )
        }
    }


}