package com.example.gitusersassignment.usersearchscreen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.example.gitusersassignment.R
import com.example.gitusersassignment.usersearchscreen.contracts.UserScreenContract
import com.example.gitusersassignment.usersearchscreen.datamodels.UserDetailViewModel
import com.example.gitusersassignment.usersearchscreen.viewmodels.UserScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    userScreenViewModel: UserScreenViewModel = hiltViewModel(),
    searchQuery: String
) {
    val state by userScreenViewModel.uiState.collectAsStateWithLifecycle()
    userScreenViewModel.setEvent(UserScreenContract.UserScreenEvent.GetUserDetails(searchQuery))

    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Profile")
                }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileDetailsStateComposable(
                userDetailViewState = state.userDetailViewState,
                modifier = Modifier
            )
        }
    }
}


@Composable
fun ProfileDetailsStateComposable(
    userDetailViewState: UserScreenContract.GetUserDetailViewState,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (userDetailViewState) {
            is UserScreenContract.GetUserDetailViewState.Error -> LoadingComponent(
                isLoading = false,
                modifier = modifier
            )

            UserScreenContract.GetUserDetailViewState.Loading -> LoadingComponent(
                isLoading = true,
                modifier = modifier
            )

            is UserScreenContract.GetUserDetailViewState.Success -> {
                LoadingComponent(
                    isLoading = false,
                    modifier = modifier
                )
                userDetailViewState.userDetailViewModel?.let {
                    ProfileDetails(userDetailViewModel = it, modifier = modifier)
                }
            }

            else -> {
                // Do nothing
            }
        }

    }

}

@Composable
fun ProfileDetails(userDetailViewModel: UserDetailViewModel, modifier: Modifier) {
    Spacer(modifier = modifier.height(20.dp))
    Card(
        shape = CircleShape, modifier = modifier
            .height(80.dp)
            .width(80.dp)
    ) {
        SubcomposeAsyncImage(
            model = userDetailViewModel.avatarUrl,
            contentDescription = "profile image",
            loading = {
                CircularProgressIndicator()
            }
        )
    }
    Spacer(modifier = modifier.height(10.dp))
    Text(
        text = userDetailViewModel.name ?: "",
        fontSize = 20.sp,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center
    )

    userDetailViewModel.location?.let {
        if (it.isNotEmpty()) {
            Spacer(modifier = modifier.height(5.dp))
            ImageAndTextComposable(
                imageResourceId = R.drawable.location_icon,
                text = it
            )
        }
    }
    userDetailViewModel.twitterUsername?.let {
        if (it.isNotEmpty()) {
            Spacer(modifier = modifier.height(5.dp))
            ImageAndTextComposable(
                imageResourceId = R.drawable.x_logo_twitter,
                text = it
            )
        }
    }
}

@Composable
fun ImageAndTextComposable(imageResourceId: Int, text: String) {
    Row(
        modifier = Modifier.wrapContentSize()
    ) {
        Image(
            painterResource(id = imageResourceId),
            contentDescription = "profile image",
            modifier = Modifier
                .height(20.dp)
                .width(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }
}