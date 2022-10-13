package com.jo.mvicleandemo.auth_flow.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class PhoneNumber(
    var countryCode: String = "",
    val phoneNumber: String = ""
) : Parcelable {
    val fullPhoneNumber
        get() =
            if (countryCode.isNotEmpty() && phoneNumber.isNotEmpty())
                "($countryCode) $phoneNumber"
            else
                ""
}