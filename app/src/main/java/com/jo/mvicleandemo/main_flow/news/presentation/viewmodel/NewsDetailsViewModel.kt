package com.jo.mvicleandemo.main_flow.news.presentation.viewmodel


import androidx.lifecycle.SavedStateHandle
import com.jo.core.domin.DataState
import com.jo.core.presentation.result.CommonResult
import com.jo.core.presentation.viewmodel.MVIBaseViewModel
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.domain.usecase.LoadNewsDetailsUseCase
import com.jo.mvicleandemo.main_flow.news.presentation.action.NewsAction
import com.jo.mvicleandemo.main_flow.news.presentation.result.NewsDetailsResult
import com.jo.mvicleandemo.main_flow.news.presentation.viewstate.NewsDetailsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadNewsDetailsUseCase: LoadNewsDetailsUseCase
) : MVIBaseViewModel<NewsAction, NewsDetailsResult, NewsDetailsViewState>() {


    override val defaultViewState: NewsDetailsViewState
        get() = NewsDetailsViewState(isIdle = true)

    override fun handleAction(action: NewsAction): Flow<NewsDetailsResult> {
        return flow {
            if (action is NewsAction.LoadNewsDetails) {
                handleActionOfLoadNewsDetails(this, action)
            }
        }
    }

    private suspend fun handleActionOfLoadNewsDetails(
        flowCollector: FlowCollector<NewsDetailsResult>,
        action: NewsAction.LoadNewsDetails
    ) {
        flowCollector.emit(CommonResult.Loading())
        val useCaseResponse = loadNewsDetailsUseCase(action.request)
        handleUseCaseResponse(useCaseResponse, action, flowCollector)
    }

    private suspend fun handleUseCaseResponse(
        useCaseResponse: DataState<Post>,
        action: NewsAction.LoadNewsDetails,
        flowCollector: FlowCollector<NewsDetailsResult>
    ) {
        when {
            (useCaseResponse is DataState.Success) -> {
                flowCollector.emit(
                    CommonResult.Success(useCaseResponse.data)
                )
            }

            (useCaseResponse is DataState.Error) -> {
                flowCollector.emit(
                    CommonResult.Error(useCaseResponse.throwable)
                )
            }

            else -> {
                flowCollector.emit(CommonResult.Empty())
            }
        }
    }

}