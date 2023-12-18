package com.example.gitusersassignment.usersearchscreen.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.gitusersassignment.usersearchscreen.contracts.UserScreenContract
import com.example.gitusersassignment.usersearchscreen.datamodels.UserViewModel
import com.example.gitusersassignment.usersearchscreen.viewmodels.UserScreenViewModel

@Composable
fun UserScreenComponent(
    viewModel: UserScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val searchTextState = remember { mutableStateOf(value = "") }
    val userScreenState by viewModel.uiState.collectAsStateWithLifecycle()
    if (userScreenState.usersViewState is UserScreenContract.GetUsersViewState.Idle)
        viewModel.setEvent(UserScreenContract.UserScreenEvent.GetUsersList)
    val modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
    Surface(
        modifier = modifier,
    ) {
        Column {
            SearchComponent(searchTextState.value) { newSearchText ->
                searchTextState.value = newSearchText
                when {
                    newSearchText.length > 1 -> {
                        viewModel.setEvent(
                            UserScreenContract.UserScreenEvent.GetUserDetails(
                                newSearchText
                            )
                        )
                    }
                }
            }
            UserListComponent(usersViewState = userScreenState.usersViewState, navController)
        }
    }
}

@Composable
fun UserListItem(user: UserViewModel, modifier: Modifier, navController: NavController) {
    Row(
        modifier = modifier
            .clickable {
                navController.navigate("User Details")
            }
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
fun UserListComponent(
    usersViewState: UserScreenContract.GetUsersViewState,
    navController: NavController
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


