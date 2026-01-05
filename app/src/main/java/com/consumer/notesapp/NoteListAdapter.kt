package com.consumer.notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteListAdapter(private val listener: (Note) -> Unit) : RecyclerView.Adapter<NoteListAdapter.VH>() {
    private var items: List<Note> = emptyList()
    fun setItems(data: List<Note>) { items = data; notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val n = items[position]
        holder.title.text = n.title ?: "(no title)"
        holder.sub.text = if (n.isSecret) "(secret)" else (n.content ?: "")
        holder.itemView.setOnClickListener { listener(n) }
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(android.R.id.text1)
        val sub: TextView = itemView.findViewById(android.R.id.text2)
    }
}
