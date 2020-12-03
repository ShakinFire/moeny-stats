package com.example.money_stats.db

import androidx.room.Embedded
import androidx.room.Relation
import com.example.money_stats.db.Category
import com.example.money_stats.db.Entry

data class CategoryAndEntry(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val  entry: List<Entry?>
)