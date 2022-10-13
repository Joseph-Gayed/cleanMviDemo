package com.jo.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jo.core.Action
import com.jo.core.Result
import com.jo.core.ViewState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * Base class for all view models that follow the MVI architecture
 */
abstract class MVIBaseViewModel<A : Action, R : Result<VS>, VS : ViewState> : ViewModel() {

    //default state
    abstract val defaultViewState: VS

    // stream of actions (intents)
    private val actionsChannel = Channel<A>(Channel.UNLIMITED)

    // state flow of view states
    @Suppress("LeakingThis")
    private val _viewStates = MutableStateFlow<VS>(defaultViewState)
    val viewStates: StateFlow<VS> = _viewStates

    //SharedFlow version from viewStates Stateflow to to be used with single events like navigation.
    val viewEvents: SharedFlow<VS> = viewStates.shareIn(
        viewModelScope,
        SharingStarted.Lazily,
        replay = 0
    )


    init {
        observeOnActionsChannel()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


    private fun observeOnActionsChannel() {
        viewModelScope.launch {
            supervisorScope {
                actionsChannel.consumeEach { action ->
                    launch {
                        val results: Flow<R> = handleAction(action)
                        results.collect { result: R ->
                            emitState {
                                reduce(result)//covert result to view state
                            }
                        }
                    }
                }
            }
        }
    }

    @Synchronized
    private fun emitState(
        stateReducer: (oldState: VS) -> VS
    ) {
        val newState = stateReducer(_viewStates.value)
        if (_viewStates.value != newState) {
            _viewStates.update {
                newState
            }
        }
    }

    fun executeAction(action: A) {
        viewModelScope.launch {
            actionsChannel.send(action)
        }
    }

    abstract fun handleAction(action: A): Flow<R>

    open fun reduce(result: R): VS {
        return result.reduce(defaultState = defaultViewState, oldState = viewStates.value)
    }

}