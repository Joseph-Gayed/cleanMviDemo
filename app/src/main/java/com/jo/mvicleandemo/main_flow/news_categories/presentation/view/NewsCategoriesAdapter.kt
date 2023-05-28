package com.jo.mvicleandemo.main_flow.news_categories.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import com.jo.core.presentation.utils.AdapterStatus
import com.jo.core.presentation.utils.BaseRecyclerViewPaginationAdapter
import com.jo.core.presentation.utils.addClickListener
import com.jo.mvicleandemo.databinding.ItemCategoryBinding
import com.jo.mvicleandemo.databinding.ItemCategoryHomeBinding
import com.jo.mvicleandemo.databinding.ViewPartialLoadingBinding
import com.jo.mvicleandemo.main_flow.news_categories.domain.model.Category

class NewsCategoriesAdapter(
    @CategoryViewType
    private val categoryViewType: Int,
    private val itemClickListener: (item: Category, position: Int) -> Unit
) : BaseRecyclerViewPaginationAdapter<Category, RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AdapterStatus.Normal -> {
                val viewHolder = getViewHolder(parent, categoryViewType)

                viewHolder.addClickListener { position, _ ->
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

    private fun getViewHolder(
        parent: ViewGroup,
        @CategoryViewType viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            CategoryViewType.HOME -> {
                val itemBinding = ItemCategoryHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                HomeNewsCategoriesItemViewHolder(itemBinding)
            }

            else -> {
                val itemBinding = ItemCategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                NewsCategoriesListItemViewHolder(itemBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsCategoriesListItemViewHolder -> {
                holder.bind(items[position])
            }
            is HomeNewsCategoriesItemViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    @IntDef(CategoryViewType.LIST, CategoryViewType.HOME)
    annotation class CategoryViewType {
        companion object {
            const val LIST = 0
            const val HOME = 1
        }
    }
}

