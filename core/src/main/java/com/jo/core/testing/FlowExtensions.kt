package com.jo.core.testing

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals


@ExperimentalCoroutinesApi
fun <T> Flow<T>.test2(expectedValues: List<T>) = runBlockingTest {
    val actualValues = mutableListOf<T>()
    val job = launch {
        collect {
            actualValues.add(it)
        }
    }
    job.cancelAndJoin()
    assertEquals(expectedValues.size, actualValues.size)
    assertEquals(expectedValues, actualValues)
}


@ExperimentalCoroutinesApi
suspend fun <T> Flow<T>.testFlow(expectedValues: List<T>) = runBlockingTest {
    val actualValues: ArrayList<T> = arrayListOf()
    val job: Job = launch {
        collect { actualValue ->
            println(actualValue)

            actualValues.add(actualValue)
        }
    }
    job.cancel()

    println("===========================")

    for (value in actualValues) {
        println(value)
    }
    assertEquals(expectedValues.size, actualValues.size)
    assertEquals(expectedValues, actualValues)
}

fun <T> Flow<T>.test3(
    expectedStates: List<T>
): Unit = runBlockingTest {
    val actualStates = mutableListOf<T>()

    val stateCollectionJob = launch {
        toList(actualStates)
    }


    expectedStates.zip(actualStates) { expectedState, actualState ->
        System.out.println("expected___:$expectedState")
        System.out.println("actual___:$actualState")
//        assertEquals(expectedState, actualState)
    }
//    assertEquals(expectedStates.size, actualStates.size)
    stateCollectionJob.cancel()
}
