package com.example.gitusersassignment.usersearchscreen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun UserDetailsScreen(
    userScreenViewModel: UserScreenViewModel = hiltViewModel()
) {
    val state by userScreenViewModel.uiState.collectAsStateWithLifecycle()
    val modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
    Surface(modifier = modifier) {
        ProfileDetailsStateComposable(
            userDetailViewState = state.userDetailViewState,
            modifier = modifier
        )
    }
}


@Composable
fun ProfileDetailsStateComposable(
    userDetailViewState: UserScreenContract.GetUserDetailViewState,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .height(200.dp)
            .background(MaterialTheme.colorScheme.onPrimary)
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
        text = userDetailViewModel.name,
        fontSize = 20.sp,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = modifier.height(5.dp))
    ImageAndTextComposable(
        imageResourceId = R.drawable.location_icon,
        text = userDetailViewModel.location,
        modifier = modifier
    )
    Spacer(modifier = modifier.height(5.dp))
    ImageAndTextComposable(
        imageResourceId = R.drawable.x_logo_twitter,
        text = userDetailViewModel.twitterUsername,
        modifier = modifier
    )
}

@Composable
fun ImageAndTextComposable(imageResourceId: Int, text: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .width(200.dp)
            .height(20.dp)
    ) {
        Image(
            painterResource(id = imageResourceId),
            contentDescription = "profile image",
            modifier = modifier.fillMaxSize()
        )
        Spacer(modifier = modifier.width(5.dp))
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }
}