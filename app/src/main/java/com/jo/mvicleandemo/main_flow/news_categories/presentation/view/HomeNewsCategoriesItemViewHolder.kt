package com.jo.mvicleandemo.main_flow.news_categories.presentation.view

import androidx.recyclerview.widget.RecyclerView
import com.jo.mvicleandemo.databinding.ItemCategoryHomeBinding
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category

class HomeNewsCategoriesItemViewHolder(private val binding: ItemCategoryHomeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Category) {
        binding.tvTitle.text = post.title
    }
}