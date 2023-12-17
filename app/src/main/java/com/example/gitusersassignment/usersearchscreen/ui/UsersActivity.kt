package com.example.gitusersassignment.usersearchscreen.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.gitusersassignment.ui.theme.GitUsersAssignmentTheme
import com.example.gitusersassignment.usersearchscreen.contracts.UserScreenContract
import com.example.gitusersassignment.usersearchscreen.datamodels.UserViewModel
import com.example.gitusersassignment.usersearchscreen.viewmodels.UserScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val userScreenViewModel: UserScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitUsersAssignmentTheme {
                // A surface container using the 'background' color from the theme
                UserScreenComponent()
            }
        }
        userScreenViewModel.setEvent(UserScreenContract.UserScreenEvent.GetUsersList)
    }

    @Preview
    @Composable
    fun UserScreenComponent() {
        val searchTextState = remember { mutableStateOf(value = "") }
        val getUsersState =
            remember { mutableStateOf(UserScreenContract.UserScreenState.initialState) }
        getUsers { state -> getUsersState.value = state }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Column {
                SearchComponent(searchTextState.value) { newSearchText ->
                    searchTextState.value = newSearchText
                }
                UserListComponent(usersViewState = getUsersState.value.usersViewState)
            }
        }
    }

    @Composable
    fun UserListItem(user: UserViewModel) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(10.dp)
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.CenterVertically)

            ) {
                SubcomposeAsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "user avatar",
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                )
            }
            Text(
                text = user.userName,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 30.dp)
            )
        }
    }

    @Composable
    fun SearchComponent(searchText: String, onValueChanged: (newValue: String) -> Unit) {
        Box(
            contentAlignment = Alignment.Center
        ) {

            TextField(
                value = searchText,
                onValueChange = { onValueChanged(it) },
                label = { Text(text = "Search Users") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)

            )
        }
    }

    @Composable
    fun UserListComponent(usersViewState: UserScreenContract.GetUsersViewState) {
        val modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        Surface(
            modifier = modifier
        ) {
            when (usersViewState) {
                UserScreenContract.GetUsersViewState.Loading -> {
                    LoadingComponent(isLoading = true, modifier)
                }

                is UserScreenContract.GetUsersViewState.Success -> {
                    LoadingComponent(isLoading = false, modifier)
                    usersViewState.userViewModel?.let { user ->
                        LazyColumn(modifier = modifier) {
                            items(user) {
                                UserListItem(user = it)
                                Divider(thickness = 2.dp)
                            }
                        }
                    }
                }

                else -> { // Do nothing }
                }
            }
        }
    }

    @Composable
    fun LoadingComponent(isLoading: Boolean, modifier: Modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            CircularProgressIndicator(modifier = Modifier.alpha(if (isLoading) 1F else 0F))
        }
    }

    private fun getUsers(usersResponse: (UserScreenContract.UserScreenState) -> Unit) =
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                userScreenViewModel.uiState.collect { state ->
                    usersResponse(state)
                }
            }
        }

}