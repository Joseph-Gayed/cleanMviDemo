package com.jo.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * there will be 2 wrapper classes 1 for real code [MyCoroutineDispatchers]
 * and the other for unit testing
 * *
 */
interface CoroutineDispatchers {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}

/**
 * Wrapper class , wraps the [Dispatchers]
 * for the real usage inside the code
 */
class MyCoroutineDispatchers(
    override val io: CoroutineDispatcher = Dispatchers.IO,
    override val main: CoroutineDispatcher = Dispatchers.Main,
    override val default: CoroutineDispatcher = Dispatchers.Default
) : CoroutineDispatchers

/**
 * Wrapper class , wraps the [Dispatchers]
 * for the unit testing to replace the io , main with [TestCoroutineDispatcher]
 */
class MyTestCoroutineScopeDispatchers @OptIn(ExperimentalCoroutinesApi::class) constructor(
    override val io: CoroutineDispatcher = UnconfinedTestDispatcher(),
    override val main: CoroutineDispatcher = UnconfinedTestDispatcher(),
    override val default: CoroutineDispatcher = UnconfinedTestDispatcher()
) : CoroutineDispatchers