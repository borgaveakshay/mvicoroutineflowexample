package com.example.gitusersassignment.usersearchscreen.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.gitusersassignment.base.mvi.BaseViewModel
import com.example.gitusersassignment.usersearchscreen.contracts.UserScreenContract
import com.example.gitusersassignment.usersearchscreen.datamodels.toState
import com.example.gitusersassignment.usersearchscreen.repositories.impl.UserScreenProcessor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserScreenViewModel @Inject constructor(private val userScreenProcessor: UserScreenProcessor) :
    BaseViewModel<UserScreenContract.UserScreenEvent, UserScreenContract.UserScreenState>() {

    override fun createInitialState(): UserScreenContract.UserScreenState =
        UserScreenContract.UserScreenState.initialState


    override fun handleIntent(event: UserScreenContract.UserScreenEvent) {
        when (event) {
            UserScreenContract.UserScreenEvent.GetUsersList -> handleGetUsersEvent()
            is UserScreenContract.UserScreenEvent.GetUserDetails -> handleGetUserDetails(event.name)
        }
    }

    private fun handleGetUsersEvent() = viewModelScope.launch {
        userScreenProcessor.getGithubUsers().collect { result ->
            setUiState { copy(usersViewState = result.toState()) }
        }
    }

    private fun handleGetUserDetails(name: String) = viewModelScope.launch {
        userScreenProcessor.getGithubUserDetails(name).collect { result ->
            setUiState { copy(userDetailViewState = result.toState()) }
        }
    }
}