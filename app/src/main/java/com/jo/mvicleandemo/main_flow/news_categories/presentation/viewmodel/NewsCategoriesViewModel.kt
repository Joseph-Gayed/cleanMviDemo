package com.jo.mvicleandemo.main_flow.news_categories.presentation.viewmodel


import com.jo.core.domin.DataState
import com.jo.core.presentation.result.CommonPaginationResult
import com.jo.core.presentation.result.CommonResult
import com.jo.core.presentation.viewmodel.MVIBaseViewModel
import com.jo.mvicleandemo.app_core.AppConstants.DEFAULT_FIRST_PAGE
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category
import com.jo.mvicleandemo.main_flow.news_categories.domain.usecase.LoadNewsCategoriesUseCase
import com.jo.mvicleandemo.main_flow.news_categories.presentation.action.NewsCategoriesAction
import com.jo.mvicleandemo.main_flow.news_categories.presentation.result.NewsCategoriesResult
import com.jo.mvicleandemo.main_flow.news_categories.presentation.viewstate.NewsCategoriesViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class NewsCategoriesViewModel @Inject constructor(
    private val loadNewsCategoriesUseCase: LoadNewsCategoriesUseCase
) : MVIBaseViewModel<NewsCategoriesAction, NewsCategoriesResult, NewsCategoriesViewState>() {


    override val defaultViewState: NewsCategoriesViewState
        get() = NewsCategoriesViewState(isIdle = true)

    init {
        executeAction(
            NewsCategoriesAction.LoadNewsCategories(
                RequestWithParams(
                    input = 0,
                    isRefreshing = false
                )
            )
        )
    }

    override fun handleAction(action: NewsCategoriesAction): Flow<NewsCategoriesResult> {
        return flow {
            if (action is NewsCategoriesAction.LoadNewsCategories) {
                handleActionOfLoadCategories(this, action)
            }
        }
    }

    private suspend fun handleActionOfLoadCategories(
        flowCollector: FlowCollector<NewsCategoriesResult>,
        action: NewsCategoriesAction.LoadNewsCategories
    ) {
        showLoading(action, flowCollector)
        val useCaseResponse = loadNewsCategoriesUseCase()
        handleUseCaseResponse(useCaseResponse, flowCollector)
    }


    private suspend fun showLoading(
        action: NewsCategoriesAction.LoadNewsCategories,
        flowCollector: FlowCollector<NewsCategoriesResult>
    ) {
        when {
            (action.request.isRefreshing) -> {
                flowCollector.emit(CommonResult.Refreshing())
            }

            else ->
                flowCollector.emit(CommonResult.Loading())
        }
    }

    private suspend fun handleUseCaseResponse(
        useCaseResponse: DataState<List<Category>>,
        flowCollector: FlowCollector<NewsCategoriesResult>
    ) {
        when {
            (useCaseResponse is DataState.Success && useCaseResponse.data.isNotEmpty()) -> {
                handleSuccessResponse(flowCollector, useCaseResponse)
            }

            (useCaseResponse is DataState.Error) -> {
                handleErrorResponse(flowCollector, useCaseResponse)
            }

            else -> {
                handleEmptyResponse(flowCollector)
            }
        }
    }

    private suspend fun handleSuccessResponse(
        flowCollector: FlowCollector<NewsCategoriesResult>,
        useCaseResponse: DataState.Success<List<Category>>
    ) {
        flowCollector.emit(
            CommonResult.Success(
                useCaseResponse.data
            )
        )
    }

    private suspend fun handleErrorResponse(
        flowCollector: FlowCollector<NewsCategoriesResult>,
        useCaseResponse: DataState.Error<List<Category>>
    ) {
        flowCollector.emit(CommonResult.Error(useCaseResponse.throwable))
    }

    private suspend fun handleEmptyResponse(
        flowCollector: FlowCollector<NewsCategoriesResult>
    ) {
        flowCollector.emit(CommonResult.Empty())
    }

    fun isLoading() =
        viewStates.value.isRefreshing || viewStates.value.isLoading

}