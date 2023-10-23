package com.jo.mvicleandemo.auth_flow.presentation.view.login

import com.jo.core.domin.DataState
import com.jo.core.presentation.result.CommonResult
import com.jo.core.testing.areBothCommonResultsEquivalent
import com.jo.mvicleandemo.app_core.AppConstants
import com.jo.mvicleandemo.auth_flow.domain.model.AuthData
import com.jo.mvicleandemo.auth_flow.domain.model.LoginRequest
import com.jo.mvicleandemo.auth_flow.domain.usecase.LoginUseCase
import com.jo.mvicleandemo.auth_flow.presentation.action.AuthAction
import com.jo.mvicleandemo.auth_flow.presentation.result.AuthResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private val loginUseCase: LoginUseCase = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `handleAction with Login action emits correct result`() = runBlocking {
        // Arrange
        val email = "test@test.com"
        val password = "password"
        val loginRequest = LoginRequest(email, password)
        val dataState: DataState<AppConstants.VerificationStatus> =
            DataState.Success(AppConstants.VerificationStatus.VERIFIED)
        coEvery { loginUseCase(loginRequest) } returns dataState

        val expectedResults = mutableListOf(
            CommonResult.Loading(),
            CommonResult.Success(data = AuthData(verificationStatus = AppConstants.VerificationStatus.VERIFIED))
        )

        // Act
        val action = AuthAction.Login(loginRequest)
        viewModel.executeAction(action)
        val actualResults =viewModel.handleAction(action).toList()


        // Assert
        coVerify(/*exactly = 1*/) { loginUseCase(loginRequest) }
        val areBothResultsEqual: Boolean = areBothCommonResultsEquivalent(expectedResults, actualResults)
        Assert.assertTrue(areBothResultsEqual)
    }


}