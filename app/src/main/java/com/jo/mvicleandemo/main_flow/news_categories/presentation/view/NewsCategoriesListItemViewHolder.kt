package com.jo.mvicleandemo.main_flow.news_categories.presentation.view

import androidx.recyclerview.widget.RecyclerView
import com.jo.mvicleandemo.databinding.ItemCategoryBinding
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category

class NewsCategoriesListItemViewHolder(private val binding: ItemCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Category) {
        binding.tvTitle.text = post.title
    }
}