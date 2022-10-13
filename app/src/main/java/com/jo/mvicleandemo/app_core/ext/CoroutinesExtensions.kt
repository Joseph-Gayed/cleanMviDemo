package com.jo.mvicleandemo.app_core.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.delayTask(
    coroutinesContext: CoroutineContext,
    delayDurationInSeconds: Long,
    jobToExecuteWhileTimerIsRunning: (() -> Unit)? = null,
    jobToExecuteAfterTimerIsFinished: () -> Unit,
    timerObserver: ((Long) -> Unit)? = null
) {
    launch(coroutinesContext) {
        val totalSeconds = TimeUnit.SECONDS.toSeconds(delayDurationInSeconds)
        val tickSeconds = 1
        for (second in totalSeconds downTo tickSeconds) {
            jobToExecuteWhileTimerIsRunning?.invoke()
            timerObserver?.invoke(second)
            delay(1000)
        }
        jobToExecuteAfterTimerIsFinished.invoke()
    }
}