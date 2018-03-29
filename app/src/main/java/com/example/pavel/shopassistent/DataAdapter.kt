package com.example.pavel.shopassistent

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item.view.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DataAdapter(var multiply: Int) : ListAdapter<QueryItem, DataAdapter.ViewHolder>(DiffItems()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: QueryItem = getItem(position)
        val dfs = DecimalFormatSymbols()
        val separator = dfs.decimalSeparator
        val df = DecimalFormat("0.00")
        holder.price.text = df.format(item.price).replace(separator + "00", "")
        holder.count.text = df.format(item.count).replace(separator + "00", "")
        holder.sum.text = df.format(item.sum * multiply).replace(separator + "00", "")
        holder.sum.setBackgroundColor(if (item.minFlag == 1) 0xFFD1FFCC.toInt() else 0xFFFAFAFA.toInt())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item, parent, false))
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val price = view.price
        val count = view.count
        val sum = view.sum
    }
}
