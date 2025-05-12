package com.example.catchmycoins

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAnalysisFragment : Fragment(R.layout.fragment_expense_analysis) {

    private lateinit var backBtn: ImageView
    private lateinit var startDate: EditText
    private lateinit var endDate: EditText
    private lateinit var pieChart: PieChart
    private lateinit var transactionList: RecyclerView
    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var categoryTotalsLayout: LinearLayout
    private lateinit var transactionAdapter: ExpenseAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        backBtn = view.findViewById(R.id.backBtn)
        startDate = view.findViewById(R.id.startDate)
        endDate = view.findViewById(R.id.endDate)
        pieChart = view.findViewById(R.id.pieChart)
        categoryTotalsLayout = view.findViewById(R.id.categoryTotalsLayout)
        transactionList = view.findViewById(R.id.listOfTransactions)
        transactionAdapter = ExpenseAdapter(emptyList())
        transactionList.adapter = transactionAdapter

        dbHelper = UserDatabaseHelper(requireContext())

        // RecyclerView setup
        transactionList.layoutManager = LinearLayoutManager(requireContext())

        // Back button action
        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Date picker dialogs
        startDate.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                startDate.setText(selectedDate)
                fetchExpensesTransactions()
            }
        }

        endDate.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                endDate.setText(selectedDate)
                fetchExpensesTransactions()
            }
        }
    }

    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun fetchExpensesTransactions() {
        val start = startDate.text.toString()
        val end = endDate.text.toString()

        if (start.isNotEmpty() && end.isNotEmpty()) {
            val transactionList = dbHelper.getExpensesTransactionsByDateRange(start, end)
            val transactions = dbHelper.getExpensesTransactionsByDateRange(start, end)
            val expensesList = transactionList.map {
                Expense(
                    title = "",
                    description = it.description,
                    amount = it.amount,
                    type = it.type,
                    date = "",
                    startTime = "",
                    endTime = "",
                    imagePath= it.imagePath
                )
            }

            setupPieChart(expensesList)
            displayCategoryTotals(expensesList)
            transactionAdapter.updateData(transactions)
        }
    }

    private fun setupPieChart(expenses: List<Expense>) {
        val totalExpenses = expenses.sumByDouble { it.amount }

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(totalExpenses.toFloat(), "Total Expenses"))

        val dataSet = PieDataSet(entries, "Summary")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS.toList())
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 18f

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Expenses"
        pieChart.animateY(1000)
        pieChart.invalidate()
    }
    private fun displayCategoryTotals(expenses: List<Expense>) {
        // Clear previous views
        categoryTotalsLayout.removeAllViews()

        if (expenses.isEmpty()) {
            val noDataView = TextView(requireContext()).apply {
                text = "No expenses found for this range."
                setTextColor(Color.WHITE)
                textSize = 16f
            }
            categoryTotalsLayout.addView(noDataView)
            return
        }

        // Group by type and sum amounts
        val totalsByCategory = expenses.groupBy { it.type }
            .mapValues { entry -> entry.value.sumByDouble { it.amount.toDouble() } }

        // Add a TextView for each category total
        for ((category, total) in totalsByCategory) {
            val categoryView = TextView(requireContext()).apply {
                text = "$category: R${"%.2f".format(total)}"
                setTextColor(Color.YELLOW)
                textSize = 18f
            }
            categoryTotalsLayout.addView(categoryView)
        }
    }

}
