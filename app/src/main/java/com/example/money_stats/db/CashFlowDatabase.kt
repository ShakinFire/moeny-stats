package com.example.money_stats.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Entry::class, Category::class], version = 1)
abstract class CashFlowDatabase : RoomDatabase() {
    abstract fun CategoryDao(): CategoryDao
    abstract fun EntryDao(): EntryDao

    companion object {
        private var INSTANCE: CashFlowDatabase? = null
        private const val DB_NAME = "cash-flow"

        fun getDatabase(context: Context): CashFlowDatabase {
            if (INSTANCE == null) {
                synchronized(CashFlowDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext, CashFlowDatabase::class.java,
                            DB_NAME
                        )
                            //.allowMainThreadQueries() // Uncomment if you don't want to use RxJava or coroutines just yet (blocks UI thread)
                            .createFromAsset("databases/prepop.db")
                            .build()
                    }
                }
            }

            return INSTANCE!!
        }
    }
}