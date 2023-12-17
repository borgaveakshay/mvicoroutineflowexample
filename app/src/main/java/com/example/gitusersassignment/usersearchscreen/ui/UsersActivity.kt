package com.example.gitusersassignment.usersearchscreen.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        setUpFlows()
        userScreenViewModel.setEvent(UserScreenContract.UserScreenEvent.GetUsersList)
    }

    @Preview
    @Composable
    fun UserScreenComponent() {
        val searchTextState = remember { mutableStateOf(value = "") }
        val getUsersState =
            remember { mutableStateOf(UserScreenContract.UserScreenState.initialState) }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Column {
                SearchComponent(searchTextState.value) { newSearchText ->

                }

            }
        }
    }

    @Composable
    fun UserListItem(user: UserViewModel) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            Row(modifier = Modifier.padding(20.dp)) {
                SubcomposeAsyncImage(
                    model = user.avatarUrl,
                    loading = {
                        CircularProgressIndicator()
                    },
                    contentDescription = "user avatar"
                )
            }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)

            )
        }
    }

    @Composable
    fun UserListComponent(userList: List<UserViewModel>) {

    }

    private fun setUpFlows():  {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                userScreenViewModel.uiState.collect { state ->
                    when (state.usersViewState) {
                        UserScreenContract.GetUsersViewState.Loading -> {
                            Toast.makeText(
                                this@MainActivity, "Getting user list", Toast.LENGTH_LONG
                            ).show()
                        }

                        is UserScreenContract.GetUsersViewState.Success -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Received response successfully ${state.usersViewState.userViewModel?.size}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        else -> {
                            // Do nothing
                        }
                    }
                }
            }
        }
    }
}