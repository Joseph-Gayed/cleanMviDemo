package com.jo.core.presentation.utils

import android.view.View
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.lifecycle.*
import com.jo.core.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*


val View.lifecycleOwner: LifecycleOwner
    get() = getTag(R.id.view_lifecycle_owner) as? LifecycleOwner ?: object : LifecycleOwner,
        LifecycleEventObserver {
        private val lifecycle = LifecycleRegistry(this)

        init {
            doOnAttach {
                findViewTreeLifecycleOwner()?.lifecycle?.addObserver(this)
            }
            doOnDetach {
                findViewTreeLifecycleOwner()?.lifecycle?.removeObserver(this)
                lifecycle.currentState = Lifecycle.State.DESTROYED
            }
        }

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            lifecycle.currentState = event.targetState
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycle
        }
    }.also {
        setTag(R.id.view_lifecycle_owner, it)
    }


val View.lifecycle get() = lifecycleOwner.lifecycle
val View.lifecycleScope get() = lifecycleOwner.lifecycleScope

/**
 * Convert the user clicks into flow0
 *
 * @return  flow of the user's clicks on the view
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun View.clicksAsFlow(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        trySend(Unit)
    }
    awaitClose { setOnClickListener(null) }
}


/**
 * Method that perform debounce or throttle on user clicks.
 * filters out values that are followed by the newer values within the given timeout.
 *
 * @param durationInMilliSeconds   debounce timeout
 * @param scope coroutine  scope to be used with the flow of the button clicks
 * @param uiAction the action to update the ui with every click without any throttling.
 * @param apiAction the action to call the api , this action should be throttled or debounced.
 */
@OptIn(FlowPreview::class, DelicateCoroutinesApi::class)
fun View.debounceClicks(
    durationInMilliSeconds: Long = 250,
    scope: CoroutineScope = GlobalScope,
    uiAction: (() -> Unit)? = null,
    apiAction: (() -> Unit)? = null
) {
    val clicksFlow =
        clicksAsFlow()
            .onEach {
                scope.launch(Dispatchers.Main) {
                    uiAction?.invoke()
                }
            }
            .debounce(durationInMilliSeconds)
            .onEach {
                apiAction?.invoke()
            }
    clicksFlow
        .launchIn(scope)
}
