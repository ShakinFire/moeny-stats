package com.example.money_stats.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.money_stats.db.*

class EntriesViewModel(application: Application): AndroidViewModel(application) {
    private val entryDao: EntryDao = CashFlowDatabase.getDatabase(application).EntryDao()
    val entryList: LiveData<List<Entry>>
    val entriesAndCategory: LiveData<List<EntriesAndCategory>>

    init {
        entryList = entryDao.getAllEntries()
        entriesAndCategory = entryDao.getEntriesAndCategories()
    }

    suspend fun insert(vararg entries: Entry) {
        entryDao.insertAll(*entries)
    }
}