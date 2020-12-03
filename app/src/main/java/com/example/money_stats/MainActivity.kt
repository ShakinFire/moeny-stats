package com.example.money_stats

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.money_stats.db.*
import com.example.money_stats.viewmodels.EntriesViewModel
import kotlinx.android.synthetic.main.content_main.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false);

        val entriesViewModel = ViewModelProvider(this).get(EntriesViewModel::class.java)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val activityIntent = Intent(this, SelectCategoryActivity::class.java)
            startActivity(activityIntent)
        }

        entriesViewModel.entriesAndCategory.observe(this, Observer { entriesAndCategory: List<EntriesAndCategory> ->
            val separatedEntries = separateEntriesByDay(entriesAndCategory)
            val sortedEntries = separatedEntries.toSortedMap(compareByDescending { it })
            val cardEntries = sortedEntries.map { (key, value) ->
                var expenses = 0.0
                var income = 0.0
                val entryInfo: MutableList<EntryCardInfo> = mutableListOf()

                value.forEach { (category, entry) ->
                    entryInfo.add(EntryCardInfo(category.type, category.name, entry.amount))
                    if (category.type == "expenses") {
                        expenses += entry.amount
                    } else {
                        income += entry.amount
                    }
                }

                val createdAt = getLocalDateByMilliseconds(value[0].entry.createdAt)
                EntryCard(createdAt, expenses, income, entryInfo)
            }

            cardsListItems.layoutManager = LinearLayoutManager(this)
            cardsListItems.adapter = CardRecyclerAdapter(this, cardEntries)
        })
    }

    private fun separateEntriesByDay(entriesAndCategory: List<EntriesAndCategory>): HashMap<Int, MutableList<EntriesAndCategory>> {
        val entriesByDay = HashMap<Int, MutableList<EntriesAndCategory>>()

        entriesAndCategory.forEach {
            val dayOfEntryCreation = getLocalDateByMilliseconds(it.entry.createdAt).dayOfMonth

            if (entriesByDay.containsKey(dayOfEntryCreation)) {
                val currentDayEntriesList = entriesByDay.getValue(dayOfEntryCreation)
                currentDayEntriesList.add(it)
                entriesByDay[dayOfEntryCreation] = currentDayEntriesList
            } else {
                entriesByDay[dayOfEntryCreation] = mutableListOf(it)
            }
        }

        return entriesByDay
    }

    private fun getLocalDateByMilliseconds(entryCreationInMilliseconds: Long): LocalDate {
        val dateOfEntryCreation = Date(entryCreationInMilliseconds)

        return dateOfEntryCreation
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_currency -> {
                val currencyFragment = CurrencyFragment()
                currencyFragment.show(supportFragmentManager, "currency_dialog")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}