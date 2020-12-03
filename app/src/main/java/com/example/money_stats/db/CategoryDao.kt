package com.example.money_stats.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDao {
    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategoriesAndEntries(): LiveData<List<CategoryAndEntry>>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE id IN (:categoryIds)")
    suspend fun loadAllByIds(categoryIds: IntArray): List<Category>

    @Insert
    suspend fun insertAll(vararg categories: Category)

    @Delete
    suspend fun delete(category: Category)
}