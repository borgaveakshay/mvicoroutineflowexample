package com.example.gitusersassignment.usersearchscreen.repositories.impl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.gitusersassignment.base.ResultStatus
import com.example.gitusersassignment.usersearchscreen.api.GithubUsersAPI
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserScreenProcessorTest {

    @get:Rule
    val instantExecuteRule = InstantTaskExecutorRule()

    @MockK
    lateinit var mockGithubUsersAPI: GithubUsersAPI

    private lateinit var userScreenProcessor: UserScreenProcessor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userScreenProcessor = UserScreenProcessor(mockGithubUsersAPI)
    }

    @Test
    fun `Github get users should return successful response`() = runTest {
        // GIVEN
        coEvery { mockGithubUsersAPI.getGithubUsers() } returns UserScreenProcessorMock.getMockedGithubUsersResponse()
        // WHEN
        val result = userScreenProcessor.getGithubUsers()
        // THEN
        result.test {
            assertEquals(ResultStatus.Loading, awaitItem().status)
            val resultItem = awaitItem()
            assertEquals(ResultStatus.Success, resultItem.status)
            assertEquals(UserScreenProcessorMock.getUserViewModels(), resultItem.data)
            awaitComplete()
        }
    }

    @Test
    fun `When Github users api fails it should raise an exception`() = runTest {
        // GIVEN
        val givenErrorMessage = "Something went wrong"
        coEvery { mockGithubUsersAPI.getGithubUsers() } throws Throwable(givenErrorMessage)
        // WHEN
        val result = userScreenProcessor.getGithubUsers()
        // THEN
        result.test {
            assertEquals(ResultStatus.Loading, awaitItem().status)
            val resultItem = awaitItem()
            assertEquals(ResultStatus.Error, resultItem.status)
            assertEquals(givenErrorMessage, resultItem.error!!.message)
            awaitComplete()
        }
    }

    @Test
    fun `Github get user details should return successful response`() = runTest {
        // GIVEN
        val givenUserParam = "any user"
        coEvery { mockGithubUsersAPI.getGithubUserDetails(givenUserParam) } returns UserScreenProcessorMock.getUserDetailResponse()
        // WHEN
        userScreenProcessor.getGithubUserDetails(givenUserParam)
        advanceTimeBy(1000)
        val result  = userScreenProcessor.getGithubUserDetails(givenUserParam)
        // THEN
        result.test {
            val resultItem = awaitItem()
            assertEquals(ResultStatus.Success, resultItem.status)
            assertEquals(UserScreenProcessorMock.getUserDetailModel(), resultItem.data)
            awaitComplete()
        }
    }

    @Test
    fun `When Github user details api fails it should raise an exception`() = runTest {
        // GIVEN
        val givenErrorMessage = "Something went wrong"
        val givenUserName = "any user"
        coEvery { mockGithubUsersAPI.getGithubUserDetails(givenUserName) } throws Throwable(givenErrorMessage)
        // WHEN
        val result = userScreenProcessor.getGithubUserDetails(givenUserName)
        // THEN
        advanceTimeBy(1000)
        result.test {
            val resultItem = awaitItem()
            assertEquals(ResultStatus.Error, resultItem.status)
            assertEquals(givenErrorMessage, resultItem.error!!.message)
            awaitComplete()
        }
    }
}