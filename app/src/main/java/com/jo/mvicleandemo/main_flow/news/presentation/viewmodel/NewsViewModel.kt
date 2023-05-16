package com.jo.mvicleandemo.main_flow.news.presentation.viewmodel


import androidx.lifecycle.SavedStateHandle
import com.jo.core.domin.DataState
import com.jo.core.presentation.result.CommonPaginationResult
import com.jo.core.presentation.viewmodel.MVIBaseViewModel
import com.jo.mvicleandemo.app_core.AppConstants.DEFAULT_FIRST_PAGE
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationBaseResponse
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationRequest
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.domain.usecase.LoadNewsUseCase
import com.jo.mvicleandemo.main_flow.news.presentation.action.NewsAction
import com.jo.mvicleandemo.main_flow.news.presentation.result.NewsResult
import com.jo.mvicleandemo.main_flow.news.presentation.viewstate.NewsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadNewsUseCase: LoadNewsUseCase
) : MVIBaseViewModel<NewsAction, NewsResult, NewsViewState>() {


    override val defaultViewState: NewsViewState
        get() = NewsViewState(isIdle = true)

    init {
        executeAction(NewsAction.LoadNews(PaginationRequest()))
    }

    override fun handleAction(action: NewsAction): Flow<NewsResult> {
        return flow {
            if (action is NewsAction.LoadNews) {
                handleActionOfLoadNews(this, action)
            }
        }
    }

    private suspend fun handleActionOfLoadNews(
        flowCollector: FlowCollector<NewsResult>,
        action: NewsAction.LoadNews
    ) {
        showLoading(action, flowCollector)
        val useCaseResponse = loadNewsUseCase(action.request)
        handleUseCaseResponse(useCaseResponse, action, flowCollector)
    }


    private suspend fun showLoading(
        action: NewsAction.LoadNews,
        flowCollector: FlowCollector<NewsResult>
    ) {
        when {
            (action.request.isRefreshing) -> {
                flowCollector.emit(CommonPaginationResult.Refreshing())
            }

            action.request.pageNumber > DEFAULT_FIRST_PAGE ->
                flowCollector.emit(CommonPaginationResult.LoadingMore())

            else ->
                flowCollector.emit(CommonPaginationResult.Loading())
        }
    }

    private suspend fun handleUseCaseResponse(
        useCaseResponse: DataState<PaginationBaseResponse<Post>>,
        action: NewsAction.LoadNews,
        flowCollector: FlowCollector<NewsResult>
    ) {
        when {
            (useCaseResponse is DataState.Success && useCaseResponse.data.items?.isNotEmpty() == true) -> {
                handleSuccessResponse(action, flowCollector, useCaseResponse)
            }

            (useCaseResponse is DataState.Error) -> {
                handleErrorResponse(action, flowCollector, useCaseResponse)
            }

            else -> {
                handleEmptyResponse(action, flowCollector)
            }
        }
    }

    private suspend fun handleSuccessResponse(
        action: NewsAction.LoadNews,
        flowCollector: FlowCollector<NewsResult>,
        useCaseResponse: DataState.Success<PaginationBaseResponse<Post>>
    ) {

        if (action.request.pageNumber > DEFAULT_FIRST_PAGE) {
            flowCollector.emit(
                CommonPaginationResult.SuccessOfLoadingMore(
                    useCaseResponse.data.items ?: emptyList(),
                    useCaseResponse.data.nextPage <= 0
                )
            )
        } else {
            flowCollector.emit(
                CommonPaginationResult.Success(
                    useCaseResponse.data.items ?: emptyList(),
                    useCaseResponse.data.nextPage <= 0
                )
            )
        }
    }

    private suspend fun handleErrorResponse(
        action: NewsAction.LoadNews,
        flowCollector: FlowCollector<NewsResult>,
        useCaseResponse: DataState.Error<PaginationBaseResponse<Post>>
    ) {

        if (action.request.pageNumber > DEFAULT_FIRST_PAGE) {
            flowCollector.emit(
                CommonPaginationResult.ErrorOfLoadingMore(useCaseResponse.throwable)
            )
        } else {
            flowCollector.emit(
                CommonPaginationResult.Error(useCaseResponse.throwable)
            )
        }
    }

    private suspend fun handleEmptyResponse(
        action: NewsAction.LoadNews,
        flowCollector: FlowCollector<NewsResult>
    ) {
        if (action.request.pageNumber > DEFAULT_FIRST_PAGE) {
            flowCollector.emit(
                CommonPaginationResult.EmptyOfLoadingMore()
            )
        } else {
            flowCollector.emit(CommonPaginationResult.Empty())
        }
    }

    fun isLoading() =
        viewStates.value.isRefreshing || viewStates.value.isLoading

    fun isLoadingMoreDisabled(): Boolean {
        return viewStates.value.isLoadingMoreDisabled()
    }
}