package com.consumer.notesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView

class NoteListAdapter(private val listener: (Note) -> Unit) : RecyclerView.Adapter<NoteListAdapter.VH>() {
    private var items: List<Note> = emptyList()
    fun setItems(data: List<Note>) { items = data; notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val compose = ComposeView(parent.context)
        val heightDp = 140
        val heightPx = (parent.context.resources.displayMetrics.density * heightDp).toInt()
        compose.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            heightPx
        )
        return VH(compose)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val n = items[position]
        (holder.itemView as ComposeView).setContent {
            NoteCard(n) { listener(n) }
        }
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: ComposeView) : RecyclerView.ViewHolder(itemView)
}
