package com.example.money_stats

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_currency.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.URL

class CurrencyRecyclerAdapter(private val context: FragmentActivity, private val currencies: MutableList<Currency>) :
    RecyclerView.Adapter<CurrencyRecyclerAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // Creating a view without data
        val itemView = layoutInflater.inflate(R.layout.item_currency, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = currencies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // getting the view (holder) and populating it with data
        val currency = currencies[position]

        GlobalScope.launch(Dispatchers.IO) {
            val drawableImage = loadImageFromWebOperations(currency.flagUrl)

            context?.runOnUiThread {
                holder.currencyFlag?.setImageDrawable(drawableImage)
            }
        }

        holder.amount?.text = String.format("%.2f", currency.amount)
        holder.currencyAbr?.text = currency.abr
    }

    private fun loadImageFromWebOperations(url: String?): Drawable? {
        return try {
            val `is`: InputStream = URL(url).content as InputStream
            Drawable.createFromStream(`is`, "src name")
        } catch (e: Exception) {
            null
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amount: TextView? = itemView.currencyAmount
        val currencyAbr: TextView? = itemView.currencyAbr
        val currencyFlag: ImageView? = itemView.currencyFlag
    }
}