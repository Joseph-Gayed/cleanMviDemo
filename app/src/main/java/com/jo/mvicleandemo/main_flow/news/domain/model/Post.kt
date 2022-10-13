package com.jo.mvicleandemo.main_flow.news.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    @SerializedName("id")
    val id: Int? = 0,

    @SerializedName("title")
    val title: String? = "",

    @SerializedName("description")
    val description: String? = "",

    @SerializedName("publishedDate")
    val publishedDate: String? = ""
) : Parcelable