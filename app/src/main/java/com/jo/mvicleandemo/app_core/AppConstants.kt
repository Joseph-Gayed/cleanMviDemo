package com.jo.mvicleandemo.app_core

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jo.mvicleandemo.R

object AppConstants {
    const val DEFAULT_FIRST_PAGE = 1
    const val DEFAULT_PAGE_SIZE = 20
    const val APP_PREFERENCES_NAME = "se7tak.prefs"
    const val APP_DATE_PATTERN = "dd-MM-yyyy"
    const val APP_DATE_TIME_PATTERN = "dd-MM-yyyy hh:mm a"
    const val SERVER_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    val destinationsWithoutBottomNav = intArrayOf(
        R.id.newsDetailsFragment
    )

    @Keep
    enum class VerificationStatus(val value: String) {
        @SerializedName("NEEDS_VERIFICATION")
        NEEDS_VERIFICATION("NEEDS_VERIFICATION"),

        @SerializedName("PENDING_VERIFICATION")
        PENDING_VERIFICATION("PENDING_VERIFICATION"),

        @SerializedName("VERIFIED")
        VERIFIED("VERIFIED"),

        @SerializedName("REJECTED")
        REJECTED("REJECTED");

        companion object {
            val DEFAULT: VerificationStatus = VERIFIED
            operator fun invoke(rawValue: String) = values()
                .find { it.value == rawValue }
        }
    }

    @Keep
    enum class UserType(val value: String) {
        @SerializedName("CRM")
        CRM("mother"),

        @SerializedName("NonCRM")
        NonCRM("kid"),

        @SerializedName("")
        DEFAULT("");

        companion object {
            operator fun invoke(rawValue: String) = values()
                .find { it.value == rawValue }
        }
    }

    @Keep
    enum class UserGender(val value: String) {
        @SerializedName("Male")
        MALE("Male"),

        @SerializedName("Female")
        FEMALE("Female"),

        @SerializedName("")
        DEFAULT("");

        companion object {
            operator fun invoke(rawValue: String) = values()
                .find { it.value == rawValue }
        }
    }

}