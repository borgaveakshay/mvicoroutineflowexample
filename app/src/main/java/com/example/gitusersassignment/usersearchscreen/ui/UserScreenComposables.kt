package com.example.gitusersassignment.usersearchscreen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.gitusersassignment.R
import com.example.gitusersassignment.base.mvi.useDebounce
import com.example.gitusersassignment.usersearchscreen.contracts.UserScreenContract
import com.example.gitusersassignment.usersearchscreen.datamodels.UserViewModel
import com.example.gitusersassignment.usersearchscreen.responsemodel.userdetail.toUserViewModel
import com.example.gitusersassignment.usersearchscreen.viewmodels.UserScreenViewModel

@Composable
fun UserScreenComponent(
    viewModel: UserScreenViewModel = hiltViewModel(), navController: NavController
) {
    val userScreenState by viewModel.uiState.collectAsStateWithLifecycle()
    if (userScreenState.usersViewState is UserScreenContract.GetUsersViewState.Idle) viewModel.setEvent(
        UserScreenContract.UserScreenEvent.GetUsersList
    )
    val modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
    Surface(
        modifier = modifier,
    ) {
        var searchText by remember {
            mutableStateOf("")
        }
        Column {
            SearchComponent {
                searchText = it
                if (it.length > 2) {
                    viewModel.setEvent(UserScreenContract.UserScreenEvent.GetUserDetails(it))
                }
            }
            when {
                searchText.isEmpty() -> UserListComponent(
                    usersViewState = userScreenState.usersViewState, navController
                )

                else -> GetUserDetailsComponent(
                    userDetailViewState = userScreenState.userDetailViewState,
                    modifier = modifier,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun GetUserDetailsComponent(
    userDetailViewState: UserScreenContract.GetUserDetailViewState,
    modifier: Modifier,
    navController: NavController
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {

        when (userDetailViewState) {
            is UserScreenContract.GetUserDetailViewState.Error -> {
                LoadingComponent(
                    isLoading = false, modifier = modifier
                )
                userDetailViewState.exception?.let {
                    Text(
                        text = "Record not found", modifier = modifier
                    )
                }
            }

            UserScreenContract.GetUserDetailViewState.Idle -> LoadingComponent(
                isLoading = false, modifier = modifier
            )

            UserScreenContract.GetUserDetailViewState.Loading -> LoadingComponent(
                isLoading = true, modifier = modifier
            )

            is UserScreenContract.GetUserDetailViewState.Success -> {
                LoadingComponent(
                    isLoading = false, modifier = modifier
                )
                userDetailViewState.userDetailViewModel?.let { userDetailViewModel ->
                    val userViewModel = userDetailViewModel.toUserViewModel()
                    Column {
                        UserListItem(
                            user = userViewModel, modifier = modifier, navController = navController
                        )
                        Divider(thickness = 2.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun UserListItem(user: UserViewModel, modifier: Modifier, navController: NavController) {
    Row(modifier = modifier
        .clickable {
            navController.navigate("User Details")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(10.dp)

    ) {
        Card(
            shape = CircleShape, modifier = Modifier.align(Alignment.CenterVertically)

        ) {
            SubcomposeAsyncImage(model = user.avatarUrl,
                contentDescription = "user avatar",
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
                loading = {
                    Image(
                        painter = painterResource(id = R.drawable.profile_icon_placeholder),
                        contentDescription = "loading placeholder"
                    )
                })
        }
        Text(
            text = user.userName ?: "",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 30.dp)
        )
    }
}

@Composable
fun SearchComponent(onValueChanged: (newValue: String) -> Unit) {
    var searchTextState by remember { mutableStateOf(value = "") }
    Box(
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = searchTextState,
            onValueChange = { searchTextState = it },
            maxLines = 1,
            singleLine = true,
            label = { Text(text = "Search Users") },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)

        )
        searchTextState.useDebounce(onChange = { onValueChanged(it) }, delayMillis = 1000L)
    }
}

@Composable
fun UserListComponent(
    usersViewState: UserScreenContract.GetUsersViewState, navController: NavController
) {
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
                            UserListItem(user = it, modifier, navController = navController)
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


