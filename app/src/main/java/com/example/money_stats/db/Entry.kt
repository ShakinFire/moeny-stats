package com.example.money_stats.db

import androidx.room.*
import com.example.money_stats.db.Category

@Entity(tableName = "entries", foreignKeys = [
    ForeignKey(
        entity = Category::class,
        childColumns = ["category_id"],
        parentColumns = ["id"]
    )
])
data class Entry (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "category_id") val categoryId: Long
)

data class EntriesAndCategory (
    @Embedded(prefix = "c_") val category: Category,
    @Embedded val entry: Entry
)