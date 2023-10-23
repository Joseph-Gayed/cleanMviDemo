package com.jo.core.testing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals


@ExperimentalCoroutinesApi
fun <T> Flow<T>.testFlow(backgroundScope: CoroutineScope, expectedValues: List<T>) = runTest {
    val actualValues = mutableListOf<T>()
    val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        collect {
            println(it)
            actualValues.add(it)
        }
    }
    job.cancelAndJoin()
    assertEquals(expectedValues.size, actualValues.size)
    assertEquals(expectedValues, actualValues)
}




fun <T> Flow<T>.mutableStateIn(
    scope: CoroutineScope,
    initialValue: T
): MutableStateFlow<T> {
    val flow = MutableStateFlow(initialValue)

    scope.launch {
        this@mutableStateIn.collect(flow)
    }
    return flow
}
