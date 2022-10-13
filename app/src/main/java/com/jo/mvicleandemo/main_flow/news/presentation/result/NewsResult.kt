package com.jo.mvicleandemo.main_flow.news.presentation.result

import com.jo.core.presentation.result.CommonPaginationResult
import com.jo.core.presentation.result.CommonResult
import com.jo.mvicleandemo.main_flow.news.domain.model.Post

typealias NewsResult = CommonPaginationResult<Post>
