package com.example.pavel.shopassistent

import android.support.v7.util.DiffUtil

class DiffItems : DiffUtil.ItemCallback<QueryItem>() {
    override fun areItemsTheSame(oldItem: QueryItem, newItem: QueryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: QueryItem, newItem: QueryItem): Boolean {
        return oldItem.minFlag == newItem.minFlag
    }
}