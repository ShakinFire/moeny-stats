package com.example.money_stats

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.money_stats.db.Category
import com.example.money_stats.db.Entry
import com.example.money_stats.viewmodels.CategoriesViewModel
import com.example.money_stats.viewmodels.EntriesViewModel
import kotlinx.android.synthetic.main.calculator_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CalculatorFragment : Fragment(), OnCategoryClickListener {
    var calculatedSum = StringBuilder()
    var operation: Char = ' '
    var hasLeftHandSide = false
    private lateinit var entriesViewModel: EntriesViewModel
    private lateinit var category: Category

    companion object {
        fun newInstance() = CalculatorFragment()
    }
    private lateinit var viewModel: CalculatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        entriesViewModel = ViewModelProvider(this).get(EntriesViewModel::class.java)
        return inflater.inflate(R.layout.calculator_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)

        initializeButtons()
    }

    override fun onCategoryClick(clickedCategory: Category) {
        category = clickedCategory
        val resourceId = resources.getIdentifier("${category.name}_color", "drawable", activity?.packageName)
        val imageView = activity?.findViewById<ImageView?>(R.id.previewSelectedCategory)
        imageView?.setImageResource(resourceId)
    }

    private fun initializeButtons() {
        operationalButtons()
        numericalButtons()
        functionalButtons()
        calculatorButtonEqual.visibility = View.INVISIBLE
    }

    /**
     * This function initializes all of our numerical buttons from
     *  [0 - 9]
     */
    private fun numericalButtons() {

        calculatorButton1.setOnClickListener {
            appendToDigitOnScreen("1")
            onInputFinish()
        }

        calculatorButton2.setOnClickListener {
            appendToDigitOnScreen("2")
            onInputFinish()
        }

        calculatorButton3.setOnClickListener {
            appendToDigitOnScreen("3")
            onInputFinish()
        }

        calculatorButton4.setOnClickListener {
            appendToDigitOnScreen("4")
            onInputFinish()
        }

        calculatorButton5.setOnClickListener {
            appendToDigitOnScreen("5")
            onInputFinish()
        }

        calculatorButton6.setOnClickListener {
            appendToDigitOnScreen("6")
            onInputFinish()
        }

        calculatorButton7.setOnClickListener {
            appendToDigitOnScreen("7")
            onInputFinish()
        }

        calculatorButton8.setOnClickListener {
            appendToDigitOnScreen("8")
            onInputFinish()
        }

        calculatorButton9.setOnClickListener {
            appendToDigitOnScreen("9")
            onInputFinish()
        }

        calculatorButton0.setOnClickListener {
            appendToDigitOnScreen("0")
            onInputFinish()
        }

        calculatorButtonDot.setOnClickListener {
            appendToDigitOnScreen(".")
            onInputFinish()
        }
    }

    /**
     *  Insert the button been clicked onto the screen so user can see
     *  inputs for the button clicked
     */
    private fun appendToDigitOnScreen(digit: String) {

        // Add each digit to our string builder
        calculatedSum.append(digit)

        // display it on the screen of our mobile app
        calculatorResult.text = calculatedSum.toString()
    }

    private fun detachDigit() {
        if (calculatedSum.endsWith('+') || calculatedSum.endsWith('-')) {
            operation = ' '
            hasLeftHandSide = false
        }

        if (calculatedSum.isNotEmpty()) {
            calculatedSum.deleteAt(calculatedSum.lastIndex)

            if (calculatedSum.isEmpty()) {
                calculatorResult.text = "0"
            } else {
                calculatorResult.text = calculatedSum.toString()
            }
        }
    }

    /**
     *  Initialize the operation keys in our calculator like the
     *  addition key, subtraction key and the likes
     */
    private fun operationalButtons() {

        calculatorButtonPlus.setOnClickListener {
            selectOperation('+')
            onInputFinish()
        }

        calculatorButtonMinus.setOnClickListener {
            selectOperation('-')
            onInputFinish()
        }

    }

    /**
     * Handles functional operations in out application like
     * clear button, backspace button and the clear everything button
     */
    private fun functionalButtons() {

        calculatorButtonSave.setOnClickListener {
            val amount = calculatedSum.toString().replace("\\D".toRegex(), "").toDouble()
            val entryToAdd = Entry(0, amount, Date().time, category?.id)
            GlobalScope.launch(Dispatchers.IO) {
                entriesViewModel.insert(entryToAdd)
                activity?.finish()
            }
        }

        calculatorButtonEqual.setOnClickListener {
            calculateCurrentSum()
            hasLeftHandSide = false
            onInputFinish()
        }

        calculatorButtonDelete.setOnClickListener {
            detachDigit()
            onInputFinish()
        }

    }

    private fun calculateCurrentSum() {
        val currentExpression = calculatedSum.toString().split(operation)

        when (operation) {
            '+' -> {
                calculatedSum.clear()
                val newSum = currentExpression[0].toDouble() + currentExpression[1].toDouble()
                appendToDigitOnScreen(String.format("%.2f", newSum))
            }
            '-' -> {
                calculatedSum.clear()
                val newSum = currentExpression[0].toDouble() - currentExpression[1].toDouble()
                appendToDigitOnScreen(String.format("%.2f", newSum))
            }
        }
    }

    /**
     * Function to assign operational sign to our math calculations
     */
    private fun selectOperation(newOperation: Char) {

        if (calculatedSum.endsWith('+') || calculatedSum.endsWith('-')) {
            calculatedSum.deleteAt(calculatedSum.lastIndex)
            hasLeftHandSide = false
        }

        if (hasLeftHandSide) {
            calculateCurrentSum()
        }

        appendToDigitOnScreen(newOperation.toString())
        hasLeftHandSide = true
        operation = newOperation
    }

    private fun onInputFinish() {
        val currentExpression = calculatedSum.split(operation).filter {
            !it.isNullOrEmpty()
        }

        if (currentExpression.size < 2) {
            calculatorButtonSave.visibility = View.VISIBLE
            calculatorButtonEqual.visibility = View.INVISIBLE
        } else {
            calculatorButtonSave.visibility = View.INVISIBLE
            calculatorButtonEqual.visibility = View.VISIBLE
        }
    }

}