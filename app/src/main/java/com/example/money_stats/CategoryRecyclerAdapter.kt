package com.example.money_stats

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.money_stats.db.Category
import com.example.money_stats.db.CategoryAndEntry
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.item_category_list.view.*

class CategoryRecyclerAdapter(
    private val context: Context,
    private val categories: List<Category>,
    private val bottomSheetBehavior: BottomSheetBehavior<View>,
    private val calculatorFragment: CalculatorFragment
) :
    RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>()
{

    private val layoutInflater = LayoutInflater.from(context)
    private lateinit var mRecyclerView: RecyclerView
    var previousSelectedCategory: String? = null
    var previousPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_category_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = categories[position]
        // the if is because when recyclerview resets values when they get scrolled out (recycled) we have to set again the selected value (colorize it)
        val resourceId = if(previousSelectedCategory != null && position == previousPosition)
            context.resources.getIdentifier("${entry.name}_color", "drawable", context.packageName) else
            context.resources.getIdentifier(entry.name, "drawable", context.packageName)
        holder.categoryItemName?.text = entry.name.capitalize()
        holder.categoryItemIcon?.setImageResource(resourceId)
        holder.currentPosition = position
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryItemName: TextView? = itemView.categoryItemName
        val categoryItemIcon: ImageView = itemView.categoryItemIcon
        var currentPosition: Int = 0

        init {
            itemView?.setOnClickListener {
//                notifyItemChanged()
                mRecyclerView.smoothScrollToPosition(currentPosition)
                toggleIcon()
                previousPosition = currentPosition
                mRecyclerView.setPadding(0, 0, 0, context.resources.getDimensionPixelOffset(R.dimen.calculator_height))
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                calculatorFragment.onCategoryClick(categories[currentPosition])
            }
        }

        private fun toggleIcon() {
            if (previousSelectedCategory == null) {
                previousSelectedCategory = categoryItemName?.text.toString().toLowerCase()
                val resourceId = context.resources.getIdentifier("${previousSelectedCategory}_color", "drawable", context.packageName)
                categoryItemIcon?.setImageResource(resourceId)
            } else {
                // take previous and current vector id (for colored and not colored icon)
                var previousSelectedResourceId = context.resources.getIdentifier(previousSelectedCategory, "drawable", context.packageName)
                var currentSelectedResourceId = context.resources.getIdentifier("${categories[currentPosition].name}_color", "drawable", context.packageName)
                // take the previous selectedView
                val previousImageView = mRecyclerView.findViewHolderForAdapterPosition(previousPosition)?.itemView?.findViewById<ImageView>(R.id.categoryItemIcon)

                // set the new icons
                previousImageView?.categoryItemIcon?.setImageResource(previousSelectedResourceId)
                categoryItemIcon?.setImageResource(currentSelectedResourceId)

                // update previous selected category and position to the current one
                previousSelectedCategory = categories[currentPosition].name
            }
        }
    }
}