package com.jo.mvicleandemo.main_flow.news_categories.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @SerializedName("id")
    val id: Int? = 0,

    @SerializedName("title")
    val title: String? = "",

    @SerializedName("image")
    val image: String? = ""
) : Parcelable