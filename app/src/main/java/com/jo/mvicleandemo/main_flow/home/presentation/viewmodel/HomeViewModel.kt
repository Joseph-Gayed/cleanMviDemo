package com.jo.mvicleandemo.main_flow.home.presentation.viewmodel


import com.jo.core.domin.DataState
import com.jo.core.presentation.result.CommonPaginationResult
import com.jo.core.presentation.viewmodel.MVIBaseViewModel
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationBaseResponse
import com.jo.mvicleandemo.app_core.data.remote.model.PaginationRequest
import com.jo.mvicleandemo.app_core.data.remote.model.RequestWithParams
import com.jo.mvicleandemo.main_flow.home.presentation.action.HomeAction
import com.jo.mvicleandemo.main_flow.home.presentation.result.HomeResult
import com.jo.mvicleandemo.main_flow.home.presentation.viewstate.HomeViewState
import com.jo.mvicleandemo.main_flow.news.domain.model.Post
import com.jo.mvicleandemo.main_flow.news.domain.usecase.LoadNewsUseCase
import com.jo.mvicleandemo.main_flow.news.presentation.viewstate.NewsViewState
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category
import com.jo.mvicleandemo.main_flow.news_categories.domain.usecase.LoadNewsCategoriesUseCase
import com.jo.mvicleandemo.main_flow.news_categories.presentation.viewstate.NewsCategoriesViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadNewsCategoriesUseCase: LoadNewsCategoriesUseCase,
    private val loadNewsUseCase: LoadNewsUseCase
) : MVIBaseViewModel<HomeAction, HomeResult, HomeViewState>() {


    override val defaultViewState: HomeViewState
        get() = HomeViewState(isIdle = true)

    init {
        executeAction(HomeAction.LoadCategories(request = RequestWithParams(Any())))
        executeAction(HomeAction.LoadNews(request = PaginationRequest()))
    }

    override fun handleAction(action: HomeAction): Flow<HomeResult> {
        return flow {
            if (action is HomeAction.LoadCategories) {
                handleActionOfLoadCategories(this, action.request)
            }
            if (action is HomeAction.LoadNews) {
                handleActionOfLoadNews(this, action.request)
            }
        }
    }

    private suspend fun handleActionOfLoadCategories(
        flowCollector: FlowCollector<HomeResult>,
        request: RequestWithParams<Any>
    ) {
        showCategoriesLoading(request, flowCollector)
        val useCaseResponse: DataState<List<Category>> = loadNewsCategoriesUseCase()
        handleLoadCategoriesUseCaseResponse(useCaseResponse, flowCollector)
    }

    private suspend fun showCategoriesLoading(
        request: RequestWithParams<Any>,
        flowCollector: FlowCollector<HomeResult>
    ) {
        when {
            (request.isRefreshing) -> {
                flowCollector.emit(
                    HomeResult.CategoriesResult(
                        NewsCategoriesViewState(isRefreshing = true)
                    )
                )
            }

            else ->
                flowCollector.emit(
                    HomeResult.CategoriesResult(
                        NewsCategoriesViewState(isLoading = true)
                    )
                )
        }
    }

    private suspend fun handleLoadCategoriesUseCaseResponse(
        useCaseResponse: DataState<List<Category>>,
        flowCollector: FlowCollector<HomeResult>
    ) {
        val categoryViewState: NewsCategoriesViewState = when {

            (useCaseResponse is DataState.Loading) -> {
                NewsCategoriesViewState(isLoading = true)
            }

            (useCaseResponse is DataState.Success) -> {
                NewsCategoriesViewState(isSuccess = true, data = useCaseResponse.data)
            }

            (useCaseResponse is DataState.Error) -> {
                NewsCategoriesViewState(error = useCaseResponse.throwable)
            }

            else -> {
                NewsCategoriesViewState(isEmpty = true)
            }
        }
        flowCollector.emit(HomeResult.CategoriesResult(categoryViewState))
    }

    private suspend fun handleActionOfLoadNews(
        flowCollector: FlowCollector<HomeResult>,
        request: PaginationRequest
    ) {
        val useCaseResponse: DataState<PaginationBaseResponse<Post>> = loadNewsUseCase(request)
        handleLoadNewsUseCaseResponse(useCaseResponse, flowCollector)
    }

    private suspend fun handleLoadNewsUseCaseResponse(
        useCaseResponse: DataState<PaginationBaseResponse<Post>>,
        flowCollector: FlowCollector<HomeResult>
    ) {
        val offerViewState: NewsViewState = when {
            (useCaseResponse is DataState.Loading) -> {
                NewsViewState(isLoading = true)
            }

            (useCaseResponse is DataState.Success) -> {
                NewsViewState(data = useCaseResponse.data.items)
            }

            (useCaseResponse is DataState.Error) -> {
                NewsViewState(error = useCaseResponse.throwable)
            }

            else -> {
                NewsViewState(isEmpty = true)
            }
        }
        flowCollector.emit(HomeResult.NewsResult(offerViewState))
    }


    fun isLoadingMoreDisabled(): Boolean {
        return viewStates.value.newsViewState.isLoadingMoreDisabled()
    }
}