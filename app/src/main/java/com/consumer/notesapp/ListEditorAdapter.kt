package com.consumer.notesapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListEditorAdapter : RecyclerView.Adapter<ListEditorAdapter.VH>() {
    data class Item(var text: String, var checked: Boolean = false)
    private val items = mutableListOf<Item>()

    fun setLines(text: String?) {
        items.clear()
        if (!text.isNullOrEmpty()) {
            text.split('\n').forEach { items.add(Item(it)) }
        }
        notifyDataSetChanged()
    }

    fun getJoined(numbered: Boolean): String {
        return items.mapIndexed { i, it ->
            val prefix = if (numbered) "${i+1}. " else "- "
            if (it.checked) prefix + "~~${it.text}~~" else prefix + it.text
        }.joinToString("\n")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.text.text = item.text
        holder.text.paintFlags = if (item.checked) holder.text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else holder.text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        holder.itemView.setOnClickListener { item.checked = !item.checked; notifyItemChanged(position) }
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(android.R.id.text1)
    }
}
