package com.example.money_stats.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EntryDao {
    @Query("SELECT * FROM entries")
    fun getAllEntries(): LiveData<List<Entry>>

    @Query("SELECT entries.amount, entries.created_at, entries.id, entries.category_id, categories.id as c_id, categories.name as c_name, categories.type as c_type FROM entries JOIN categories ON (categories.id = entries.category_id)")
    fun getEntriesAndCategories(): LiveData<List<EntriesAndCategory>>

    @Insert
    suspend fun insertAll(vararg entries: Entry)

    @Delete
    suspend fun delete(entry: Entry)
}