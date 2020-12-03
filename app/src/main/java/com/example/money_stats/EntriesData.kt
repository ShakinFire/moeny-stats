package com.example.money_stats

import com.example.money_stats.db.Category
import java.time.LocalDate

data class EntryCard(val date: LocalDate, val totalExpensesAmount: Double, val totalIncomeAmount: Double, val entries: MutableList<EntryCardInfo>)

data class EntryCardInfo(val categoryType: String, val categoryName: String, val amount: Double)

interface OnCategoryClickListener {
    fun onCategoryClick(category: Category)
}