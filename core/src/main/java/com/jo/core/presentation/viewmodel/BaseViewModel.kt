package com.jo.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jo.core.ViewState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*

/**
 * Base class for view models that are not follow MVI pattern
 */
abstract class BaseViewModel<VS : ViewState> : ViewModel() {

    //default state
    abstract val defaultViewState: VS

    //state observer
    private val _viewStates: MutableStateFlow<VS> by lazy { MutableStateFlow(defaultViewState) }
    val viewStates: StateFlow<VS> by lazy { _viewStates }
    val viewEvents by lazy {
        _viewStates.shareIn(
            viewModelScope,
            SharingStarted.Lazily,
            replay = 0
        )
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    @Synchronized
    fun emitState(
        stateReducer: (oldState: VS) -> VS
    ) {
        val newState = stateReducer(_viewStates.value)
        if (_viewStates.value != newState) {
            _viewStates.update {
                newState
            }
        }
    }

}