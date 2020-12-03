package com.example.money_stats

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card_list.view.*
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class CardRecyclerAdapter(private val context: Context, private val cards: List<EntryCard>) :
    RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // Creating a view without data
        val itemView = layoutInflater.inflate(R.layout.item_card_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // getting the view (holder) and populating it with data
        val card = cards[position]

        if (card.totalExpensesAmount == 0.0) {
            holder.expenses?.visibility = View.GONE
        } else {
            holder.expenses?.text = context.getString(R.string.expenses_label, abs(card.totalExpensesAmount))
        }

        if (card.totalIncomeAmount == 0.0) {
            holder.income?.visibility = View.GONE
        } else {
            holder.income?.text = context.getString(R.string.income_label, abs(card.totalIncomeAmount))
        }

        holder.date?.text = card.date.format(DateTimeFormatter.ofPattern("MM/dd E"))
        val entryLayoutManager = LinearLayoutManager(holder.recyclerView.context)
        entryLayoutManager.initialPrefetchItemCount = 1

        holder.recyclerView.apply {
            layoutManager = entryLayoutManager
            adapter = EntryRecyclerAdapter(context, card.entries.reversed())
            setRecycledViewPool(viewPool)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.entriesListItems
        val date: TextView? = itemView.entriesDate
        val expenses: TextView? = itemView.expenses
        val income: TextView? = itemView.income
    }
}