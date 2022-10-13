package com.jo.core.testing

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
fun <T> StateFlow<T>.test(testFunction: (list: List<T>) -> Unit) = runBlockingTest {
    val list = mutableListOf<T>()
    val job = launch {
        toList(list)
    }
    testFunction(list)
    job.cancel()
}

@ExperimentalCoroutinesApi
fun <T> SharedFlow<T>.test(testFunction: (list: List<T>) -> Unit) = runBlockingTest {
    val list = mutableListOf<T>()
    val job = launch {
        toList(list)
    }
    testFunction(list)
    job.cancel()
}
