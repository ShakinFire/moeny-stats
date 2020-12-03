package com.example.money_stats

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.money_stats.db.Category
import com.example.money_stats.db.CategoryAndEntry
import com.example.money_stats.viewmodels.CategoriesViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_select_category.*
import kotlinx.android.synthetic.main.content_select_category.*

class SelectCategoryActivity : AppCompatActivity() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_category)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragmentManager = supportFragmentManager
        val calculatorFragment = fragmentManager.findFragmentById(R.id.calculatorFragment) as CalculatorFragment

        val categoriesViewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        val calculatorBottomSheet = findViewById<View>(R.id.calculatorFragment)
        bottomSheetBehavior = BottomSheetBehavior.from(calculatorBottomSheet)

        val adapterEntryType = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayOf("Expenses", "Income"))
        adapterEntryType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerEntryType.adapter = adapterEntryType

        spinnerEntryType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        }

        categoriesViewModel.categoryList.observe(this, Observer { categories: List<Category> ->
            categoryItems.layoutManager = GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
            categoryItems.adapter = CategoryRecyclerAdapter(this, categories, bottomSheetBehavior, calculatorFragment)
        })

    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            categoryItems.setPadding(0, 0, 0, 0)
        } else {
            super.onBackPressed()
        }
    }
}