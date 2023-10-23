package com.jo.mvicleandemo.main_flow.news.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jo.core.CoroutineDispatchers
import com.jo.core.MyTestCoroutineScopeDispatchers
import com.jo.core.domin.DataState
import com.jo.core.presentation.result.CommonResult
import com.jo.core.presentation.view_state.CommonViewState
import com.jo.core.testing.areBothCommonResultsEquivalent
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.domain.usecase.LoadNewsDetailsUseCase
import com.jo.mvicleandemo.main_flow.news.presentation.action.NewsAction
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NewsDetailsViewModelTest {
    private lateinit var viewModel: NewsDetailsViewModel
    private val savedStateHandle: SavedStateHandle = mockk()
    private val loadNewsDetailsUseCase: LoadNewsDetailsUseCase = mockk()

    private lateinit var dispatcherProvider: CoroutineDispatchers

    private var postRequest = RequestWithParams(1)
    var postResponse = Post(id = 1, title = "Test Post", description = "Test body")

    @Before
    fun setup() {
        dispatcherProvider = MyTestCoroutineScopeDispatchers()
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = NewsDetailsViewModel(savedStateHandle, loadNewsDetailsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given LoadNewsDetails action, when view model is called, then emit Success Result`() =
        runBlocking {
            // Arrange
            coEvery { loadNewsDetailsUseCase(postRequest) } returns DataState.Success(postResponse)
            val expectedResults = mutableListOf(
                CommonResult.Loading(),
                CommonResult.Success(data = postResponse)
            )

            // Act
            val action = NewsAction.LoadNewsDetails(postRequest)
            viewModel.executeAction(action)
            val actualResults = viewModel.handleAction(action).toList()

            // Assert
            coVerify(/*exactly = 1*/) { loadNewsDetailsUseCase(postRequest) }

            val areBothResultsEqual: Boolean =
                areBothCommonResultsEquivalent(expectedResults, actualResults)
            assertTrue(areBothResultsEqual)
        }


    @Test
    fun `given LoadNewsDetails action, when loadNewsDetailsUseCase returns Error result, then emit Error Result`() =
        runTest {
            // Arrange
            val action = NewsAction.LoadNewsDetails(postRequest)
            val error = Throwable("Test error")
            coEvery { loadNewsDetailsUseCase(postRequest) } returns DataState.Error(error)

            // Act
            val actualResults = viewModel.handleAction(action).toList()

            // Assert
            assertEquals(2, actualResults.size)
            assertTrue(actualResults[0] is CommonResult.Loading)
            assertTrue(actualResults[1] is CommonResult.Error)
            assertEquals((actualResults[1] as CommonResult.Error).error, error)

            coVerify(exactly = 1) { loadNewsDetailsUseCase(postRequest) }
        }


    //there is a challenge here , During the Test , the viewStates stateflow doesn't emit all in between states, it
    //emits idle state ,and success state without the loading state

    @Test
    fun `given LoadNewsDetails action, when view model is called, then emit Success ViewState`() =
        runTest {
            // Arrange
            coEvery { loadNewsDetailsUseCase(postRequest) } returns DataState.Success(postResponse)
            val expectedViewStates: MutableList<CommonViewState<Post>> = mutableListOf(
                CommonViewState(isIdle = true),
                CommonViewState(isLoading = true),
                CommonViewState(isSuccess = true, data = postResponse)
            )
            val actualViewStates: MutableList<CommonViewState<Post>> = mutableListOf()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.viewStates.toList(actualViewStates)
            }

            // Act
            val action = NewsAction.LoadNewsDetails(postRequest)
            viewModel.executeAction(action)

            // Assert

            coVerify(/*exactly = 1*/) { loadNewsDetailsUseCase(postRequest) }
            for (state in actualViewStates) {
                println("++++++++++++++++++++++++++++$state")
            }
//            assertTrue(actualViewStates.size == 3)
            assertEquals(expectedViewStates, actualViewStates)


        }

    private suspend fun testUsingTurbine(actualViewStates: MutableList<CommonViewState<Post>>) {
        viewModel.viewStates.test {
            val idle = awaitItem()
            println("---------------$idle")
            delay(500)
            val loading = awaitItem()
            println("---------------$loading")
            val success = awaitItem()
            println("---------------$success")
            actualViewStates.addAll(listOf(idle, loading, success))
        }
    }


    /*    @Test
        fun testFlowContinuousEmission() = runTest {
            //Arrange
            val viewStates = MutableStateFlow(CommonViewState<Post>(isIdle = true))
            val actualViewStates: MutableList<CommonViewState<Post>> = mutableListOf()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewStates.toList(actualViewStates)
            }

            val expectedViewStates: MutableList<CommonViewState<Post>> = mutableListOf(
                CommonViewState(isIdle = true),
                CommonViewState(isLoading = true),
                CommonViewState(isSuccess = true, data = postResponse)
            )

            //Act
            delay(1000)
            viewStates.emit(CommonViewState(isLoading = true))
            delay(1000)
            viewStates.emit(CommonViewState(isSuccess = true, data = postResponse))

            //Assert
            assertTrue(actualViewStates.size == 3)
            assertEquals(expectedViewStates,actualViewStates)
        }*/

}



