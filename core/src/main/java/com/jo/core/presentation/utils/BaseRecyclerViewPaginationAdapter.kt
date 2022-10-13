package com.jo.core.presentation.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerViewPaginationAdapter<T, VH : RecyclerView.ViewHolder> :
    BaseRecyclerViewAdapter<T, VH>() {

    @AdapterStatus
    var adapterStatus: Int = AdapterStatus.Normal
        set(value) {
            if (field == value) return
            field = value
            if (value == AdapterStatus.Loading || value == AdapterStatus.Error) {
                notifyItemInserted(items.size - 1)
            } else {
                notifyItemInserted(items.size - 1)
            }
        }


    override fun setData(newItems: List<T>) {
        adapterStatus = AdapterStatus.Normal
        super.setData(newItems)
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) adapterStatus else AdapterStatus.Normal
    }


    override fun getItemCount(): Int {
        return if (adapterStatus == AdapterStatus.Normal) items.size else items.size + 1
    }


    inner class LoadMoreFooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

}