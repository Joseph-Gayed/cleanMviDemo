package com.jo.mvicleandemo.main_flow.news.presentation.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jo.core.presentation.utils.AdapterStatus
import com.jo.core.presentation.utils.BaseRecyclerViewPaginationAdapter
import com.jo.core.presentation.utils.addClickListener
import com.jo.mvicleandemo.databinding.ItemNewsBinding
import com.jo.mvicleandemo.databinding.ViewPartialLoadingBinding
import com.jo.mvicleandemo.main_flow.news.domain.model.Post

class NewsAdapter(private val itemClickListener: (item: Post, position: Int) -> Unit) :
    BaseRecyclerViewPaginationAdapter<Post, RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AdapterStatus.Normal -> {
                val itemBinding = ItemNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                NewsViewHolder(itemBinding).addClickListener { position, _ ->
                    itemClickListener.invoke(items[position], position)
                }
            }

            else -> {
                val itemBinding = ViewPartialLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                LoadMoreFooterViewHolder(itemBinding.root)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.tvTitle.text = post.title
            binding.tvDescription.text = post.description
            binding.tvDateTime.text = post.publishedDate
        }

    }
}