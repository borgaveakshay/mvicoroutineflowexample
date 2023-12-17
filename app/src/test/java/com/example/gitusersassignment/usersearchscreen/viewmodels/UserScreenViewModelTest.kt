package com.example.gitusersassignment.usersearchscreen.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.gitusersassignment.MainCoroutineRule
import com.example.gitusersassignment.base.Result
import com.example.gitusersassignment.usersearchscreen.api.GithubUsersAPI
import com.example.gitusersassignment.usersearchscreen.contracts.UserScreenContract
import com.example.gitusersassignment.usersearchscreen.datamodels.UserDetailViewModel
import com.example.gitusersassignment.usersearchscreen.datamodels.UserViewModel
import com.example.gitusersassignment.usersearchscreen.repositories.impl.UserScreenProcessor
import com.example.gitusersassignment.usersearchscreen.repositories.impl.UserScreenProcessorMock
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var mockGithubUsersAPI: GithubUsersAPI

    @MockK
    lateinit var userScreenProcessor: UserScreenProcessor
    private lateinit var userViewModel: UserScreenViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userViewModel = UserScreenViewModel(userScreenProcessor)
    }

    @Test
    fun `When get users event successful we should get the correct data state`() = runTest {
        // GIVEN
        coEvery { mockGithubUsersAPI.getGithubUsers() } returns UserScreenProcessorMock.getMockedGithubUsersResponse()
        coEvery { userScreenProcessor.getGithubUsers() } returns flow {
            emit(Result.success(UserScreenProcessorMock.getUserViewModels()))
            delay(10)
        }.onStart {
            emit(Result.loading())
            delay(10)
        }

        // WHEN
        userViewModel.setEvent(UserScreenContract.UserScreenEvent.GetUsersList)

        // THEN
        // Initial state is idle
        assertEquals(
            UserScreenContract.GetUsersViewState.Idle,
            userViewModel.uiState.value.usersViewState
        )
        advanceTimeBy(10)
        // After receiving the event it will be first loading the response from network
        assertEquals(
            UserScreenContract.GetUsersViewState.Loading,
            userViewModel.uiState.value.usersViewState
        )
        advanceTimeBy(10)
        // It will then receive the successful response.
        assertEquals(
            UserScreenContract.GetUsersViewState.Success(UserScreenProcessorMock.getUserViewModels()),
            userViewModel.uiState.value.usersViewState
        )

    }

    @Test
    fun `When get users event is not successful then we should get the error state`() = runTest {
        // GIVEN
        val givenErrorMessage = "Something went wrong"
        val expectedError = Result.error<List<UserViewModel>>(Throwable(givenErrorMessage))
        val flow = flow {
            emit(expectedError)
            delay(10)
        }.onStart {
            emit(Result.loading())
            delay(10)
        }
        coEvery { mockGithubUsersAPI.getGithubUsers() } returns UserScreenProcessorMock.getMockedGithubUsersResponse()
        coEvery { userScreenProcessor.getGithubUsers() } returns flow

        // WHEN
        userViewModel.setEvent(UserScreenContract.UserScreenEvent.GetUsersList)
        // THEN
        assertEquals(
            UserScreenContract.GetUsersViewState.Idle,
            userViewModel.uiState.value.usersViewState
        )
        advanceTimeBy(10)
        assertEquals(
            UserScreenContract.GetUsersViewState.Loading,
            userViewModel.uiState.value.usersViewState
        )
        advanceTimeBy(10)
        // After receiving the event it will be first loading the response from network
        // It will then receive an exception from the api.
        assertEquals(
            UserScreenContract.UserScreenEffects.ErrorEffect(
                expectedError.error
            ), userViewModel.uiEffect.first()
        )

    }

    @Test
    fun `When get user detail event is successful we should get the correct user detail data state`() =
        runTest {
            // GIVEN
            val givenUserName = "any user"
            coEvery { mockGithubUsersAPI.getGithubUserDetails(givenUserName) } returns UserScreenProcessorMock.getUserDetailResponse()
            coEvery { userScreenProcessor.getGithubUserDetails(givenUserName) } returns flow {
                emit(Result.success(UserScreenProcessorMock.getUserDetailModel()))
                delay(10)
            }.onStart {
                emit(Result.loading())
                delay(10)
            }

            // WHEN
            userViewModel.setEvent(UserScreenContract.UserScreenEvent.GetUserDetails(givenUserName))

            // THEN
            // Initial state is idle
            assertEquals(
                UserScreenContract.GetUserDetailViewState.Idle,
                userViewModel.uiState.value.userDetailViewState
            )
            advanceTimeBy(10)
            // After receiving the event it will be first loading the response from network
            assertEquals(
                UserScreenContract.GetUserDetailViewState.Loading,
                userViewModel.uiState.value.userDetailViewState
            )
            advanceTimeBy(10)
            // It will then receive the successful response.
            assertEquals(
                UserScreenContract.GetUserDetailViewState.Success(UserScreenProcessorMock.getUserDetailModel()),
                userViewModel.uiState.value.userDetailViewState
            )

        }

    @Test
    fun `When get user details event is not successful then we should get the error state`() =
        runTest {
            // GIVEN
            val givenUserName = "any user"
            val givenErrorMessage = "Something went wrong"
            val expectedError = Result.error<UserDetailViewModel>(Throwable(givenErrorMessage))
            val flow = flow {
                emit(expectedError)
                delay(10)
            }.onStart {
                emit(Result.loading())
                delay(10)
            }
            coEvery { mockGithubUsersAPI.getGithubUserDetails(givenUserName) } returns UserScreenProcessorMock.getUserDetailResponse()
            coEvery { userScreenProcessor.getGithubUserDetails(givenUserName) } returns flow

            // WHEN
            userViewModel.setEvent(UserScreenContract.UserScreenEvent.GetUserDetails(givenUserName))
            // THEN
            assertEquals(
                UserScreenContract.GetUserDetailViewState.Idle,
                userViewModel.uiState.value.userDetailViewState
            )
            advanceTimeBy(10)
            assertEquals(
                UserScreenContract.GetUserDetailViewState.Loading,
                userViewModel.uiState.value.userDetailViewState
            )
            advanceTimeBy(10)
            // After receiving the event it will be first loading the response from network
            // It will then receive an exception from the api.
            assertEquals(
                UserScreenContract.UserScreenEffects.ErrorEffect(
                    expectedError.error
                ), userViewModel.uiEffect.first()
            )

        }
}