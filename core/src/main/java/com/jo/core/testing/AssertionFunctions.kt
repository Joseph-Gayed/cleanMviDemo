package com.jo.core.testing

import com.jo.core.presentation.result.CommonResult

fun <T> areBothCommonResultsEquivalent(
    expected: List<CommonResult<T>>,
    actual: List<CommonResult<T>>
): Boolean {
    //both sizes equal
    if (expected.size != actual.size || actual.isEmpty()) return false

    //are both instance of same class
    for (i in expected.indices) {
        if (expected[i]::class.java != actual[i]::class.java) {
            return false
        }

        if ((expected[i] is CommonResult.Success)) {
            val expectedSuccessData = (expected[i] as CommonResult.Success).data
            val actualSuccessData = (actual[i] as CommonResult.Success).data

            if (expectedSuccessData != actualSuccessData) {
                return false
            }
        }
    }

    return true
}