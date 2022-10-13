package com.jo.core.presentation.utils

import android.annotation.SuppressLint
import androidx.annotation.IntDef
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<VH>() {

    var items: MutableList<T> = mutableListOf()
    var selectedPos = -1

    lateinit var diffUtil: BaseDiffUtil<T>
    lateinit var onItemClickListener: OnItemClickListener<T>

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun setData(newItems: List<T>) {
        if (::diffUtil.isInitialized)
            setDataWithDiffUtil(newItems)
        else {
            items = newItems.toMutableList()
            notifyDataSetChanged()
        }
    }

    private fun setDataWithDiffUtil(newItems: List<T>) {
        diffUtil.oldList = items
        diffUtil.newList = newItems
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    fun updateItem(position: Int, newItem: T) {
        if (position > -1 && position < items.size) {
            items[position] = newItem
            notifyItemChanged(position)
        }
    }

    fun removeItem(item: T) {
        val position = items.indexOf(item)
        removeItem(position)
    }

    interface OnItemClickListener<T> {
        fun onItemClick(pos: Int, item: T)
    }
}

abstract class BaseDiffUtil<T> : DiffUtil.Callback() {
    lateinit var oldList: List<T>
    lateinit var newList: List<T>

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

}


@IntDef(AdapterStatus.Normal, AdapterStatus.Loading, AdapterStatus.Error)
annotation class AdapterStatus {
    companion object {
        const val Normal = 0
        const val Loading = 1
        const val Error = 2
    }
}