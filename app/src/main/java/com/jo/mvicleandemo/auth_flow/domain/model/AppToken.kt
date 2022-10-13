package com.jo.mvicleandemo.auth_flow.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.jo.mvicleandemo.app_core.AppConstants
import kotlinx.parcelize.Parcelize

@Entity
@Keep
data class AppToken(
    @NonNull
    @ColumnInfo(name = "idPk")
    @PrimaryKey
    var idPk: Int = 1,

    @NonNull
    @ColumnInfo(name = "accessToken")
    @SerializedName("access_token")
    var accessToken: String = "",

    @NonNull
    @ColumnInfo(name = "refreshToken")
    @SerializedName("refresh_token")
    var refreshToken: String = "",

    @NonNull
    @ColumnInfo(name = "expiryDate")
    @SerializedName("expires_in")
    var expiryDate: Long = 0L,

    @NonNull
    @ColumnInfo(name = "refreshTokenExpiryDate")
    @SerializedName("refresh_expires_in")
    var refreshTokenExpiryDate: Long = 0L,

    @NonNull
    @ColumnInfo(name = "userType")
    @Transient
    var userType: AppConstants.UserType = AppConstants.UserType.DEFAULT,

    @NonNull
    @ColumnInfo(name = "verificationStatus")
    var verificationStatus: AppConstants.VerificationStatus = AppConstants.VerificationStatus.DEFAULT,

    @NonNull
    @ColumnInfo(name = "userName")
    var userName: String = "",

    @NonNull
    @ColumnInfo(name = "password")
    var password: String = "",

    @Transient
    var isRefreshed: Boolean = false
)