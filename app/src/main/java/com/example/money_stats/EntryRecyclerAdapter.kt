package com.example.money_stats

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_entry_list.view.*

class EntryRecyclerAdapter (private val context: Context, private val entries: List<EntryCardInfo>) :
    RecyclerView.Adapter<EntryRecyclerAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // Creating a view without data
        val itemView = layoutInflater.inflate(R.layout.item_entry_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = entries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // getting the view (holder) and populating it with data
        val entry = entries[position]
        val resourceId = context.resources.getIdentifier("${entry.categoryName}_color", "drawable", context.packageName)

        holder.categoryIcon.setImageResource(resourceId)
        holder.categoryName.text = entry.categoryName.capitalize()
        holder.entryAmount.text = String.format("%.2f", entry.amount)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName = itemView.findViewById<TextView>(R.id.categoryName)
        val entryAmount = itemView.findViewById<TextView>(R.id.entryAmount)
        val categoryIcon = itemView.categoryEntryIcon

    }
}