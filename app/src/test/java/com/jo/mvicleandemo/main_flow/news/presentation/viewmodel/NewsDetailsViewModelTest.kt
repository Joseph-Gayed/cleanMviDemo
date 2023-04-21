package com.jo.mvicleandemo.main_flow.news.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.jo.core.domin.DataState
import com.jo.core.presentation.result.CommonResult
import com.jo.core.presentation.view_state.CommonViewState
import com.jo.core.testing.MainCoroutineRule
import com.jo.core.testing.test3
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.domain.usecase.LoadNewsDetailsUseCase
import com.jo.mvicleandemo.main_flow.news.presentation.action.NewsAction
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.jupiter.api.BeforeEach

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewsDetailsViewModelTest {
    val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var loadNewsDetailsUseCase: LoadNewsDetailsUseCase

    @InjectMockKs
    private lateinit var unitUnderTest: NewsDetailsViewModel

    var postRequest = RequestWithParams(1)
    var postResponse = Post(id = 1, title = "Test Post", description = "Test body")

    @Before
    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)


    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `given LoadNewsDetails action, when view model is called, then emit Success result`() {
        // given
        val action = NewsAction.LoadNewsDetails(postRequest)
        coEvery { loadNewsDetailsUseCase(postRequest) } returns DataState.Success(postResponse)

        // when
        unitUnderTest.executeAction(action)

        // then
        val expectedStates: List<CommonViewState<Post>> = listOf(
            CommonViewState<Post>(isLoading = true),
            CommonViewState<Post>(isSuccess = true)
        )

        unitUnderTest.viewStates.test3(expectedStates)

        coVerify(exactly = 1) { loadNewsDetailsUseCase(postRequest) }
    }

//    @Test
//    fun `given LoadNewsDetails action, when view model is called, then emit Success result`() =
//        mainCoroutineRule.runBlockingTest {
//            // given
//            val action = NewsAction.LoadNewsDetails(postRequest)
//            coEvery { loadNewsDetailsUseCase(postRequest) } returns DataState.Success(postResponse)
//
//            // when
//            unitUnderTest.executeAction(action)
//
//            // then
//            unitUnderTest.viewStates.test { results ->
//                results[0] is CommonResult.Loading<*>
//                results[1] is CommonResult.Success<*>
//            }
//
//            coVerify(exactly = 1) { loadNewsDetailsUseCase(postRequest) }
//        }


    @Test
    fun `given LoadNewsDetails action, when handleAction is called, then emit Success result`() =
        runTest {
            // given
            val action = NewsAction.LoadNewsDetails(postRequest)
            coEvery { loadNewsDetailsUseCase(postRequest) } returns DataState.Success(postResponse)

            // when
            val resultsFlow: Flow<CommonResult<Post>> = unitUnderTest.handleAction(action)
            val results = resultsFlow.toList()

            // then
            Assert.assertEquals(2, results.size)
            Assert.assertTrue(results[0] is CommonResult.Loading)
            Assert.assertTrue(results[1] is CommonResult.Success)
            Assert.assertEquals((results[1] as CommonResult.Success).data, postResponse)

            coVerify(exactly = 1) { loadNewsDetailsUseCase(postRequest) }
        }


    @Test
    fun `given LoadNewsDetails action, when loadNewsDetailsUseCase returns Error result, then emit Error result`() =
        runTest {
            // given
            val action = NewsAction.LoadNewsDetails(postRequest)
            val error = Throwable("Test error")
            coEvery { loadNewsDetailsUseCase(postRequest) } returns DataState.Error(error)

            // when
            val results = unitUnderTest.handleAction(action).toList()

            // then
            Assert.assertEquals(2, results.size)
            Assert.assertTrue(results[0] is CommonResult.Loading)
            Assert.assertTrue(results[1] is CommonResult.Error)
            Assert.assertEquals((results[1] as CommonResult.Error).error, error)

            coVerify(exactly = 1) { loadNewsDetailsUseCase(postRequest) }
        }


    @Test
    fun `given LoadNewsDetails action, when loadNewsDetailsUseCase returns Loading result, then emit Loading result`() =
        runTest {
            // given
            val action = NewsAction.LoadNewsDetails(postRequest)
            coEvery { loadNewsDetailsUseCase(postRequest) } returns DataState.Loading()

            // when
            val results = unitUnderTest.handleAction(action).toList()

            // then
            Assert.assertEquals(2, results.size)
            Assert.assertTrue(results[0] is CommonResult.Loading)
            Assert.assertTrue(results[1] is CommonResult.Empty)

            coVerify(exactly = 1) { loadNewsDetailsUseCase(postRequest) }
        }
}