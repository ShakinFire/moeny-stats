package com.example.money_stats.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.money_stats.db.CategoryAndEntry
import com.example.money_stats.db.CashFlowDatabase
import com.example.money_stats.db.Category
import com.example.money_stats.db.CategoryDao

class CategoriesViewModel(application: Application): AndroidViewModel(application) {
    private val categoryDao: CategoryDao = CashFlowDatabase.getDatabase(application).CategoryDao()
    val categoryList: LiveData<List<Category>>
    val categoryAndEntriesList: LiveData<List<CategoryAndEntry>>

    init {
        categoryList = categoryDao.getAllCategories()
        categoryAndEntriesList = categoryDao.getCategoriesAndEntries()
    }

    suspend fun insert(vararg categories: Category) {
        categoryDao.insertAll(*categories)
    }
}