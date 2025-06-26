package com.example.catchmycoins

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class ExpenseVsIncomeFragment : Fragment(R.layout.fragment_expense_vs_income) {

    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var displayExpensePrice: TextView
    private lateinit var displayIncomePrice: TextView
    private lateinit var displayBalance: TextView
    private lateinit var expenseVsIncomeChart: PieChart
    private lateinit var transactionList: RecyclerView
    private lateinit var emptyTransactionText: TextView

    private lateinit var addTransaction: LinearLayout
    private lateinit var badgesEarned: LinearLayout
    private lateinit var goals: LinearLayout
    private lateinit var learnAboutMoney: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Database
        dbHelper = UserDatabaseHelper(requireContext())

        // Layout references
        displayExpensePrice = view.findViewById(R.id.displayExpensePrice)
        displayIncomePrice = view.findViewById(R.id.displayIncomePrice)
        displayBalance = view.findViewById(R.id.displayBalance)
        expenseVsIncomeChart = view.findViewById(R.id.expenseVSincome)
        transactionList = view.findViewById(R.id.transactionList)
        emptyTransactionText = view.findViewById(R.id.emptyTransactionText)

        addTransaction = view.findViewById(R.id.addTransaction)
        badgesEarned = view.findViewById(R.id.badgesEarned)
        goals = view.findViewById(R.id.goals)
        learnAboutMoney = view.findViewById(R.id.salary)

        // Click handlers
        addTransaction.setOnClickListener {
            openFragment(CategoryFragment())
        }
        badgesEarned.setOnClickListener {
            openFragment(BadgesEarnedFragment())
        }
        goals.setOnClickListener {
            openFragment(GoalsFragment())
        }
        learnAboutMoney.setOnClickListener {
            openFragment(LearnAboutMoneyFragment())
        }

        // Load data
        loadAndDisplayTransactionData()
    }

    private fun loadAndDisplayTransactionData() {
        val transactions = dbHelper.getTransactions()

        val expenses = transactions.filter { it.type == "Expense" }
        val income = transactions.filter { it.type == "Income" }

        val totalExpenses = expenses.sumOf { it.amount }
        val totalIncome = income.sumOf { it.amount }
        val balance = totalIncome - totalExpenses

        displayExpensePrice.text = "R %.2f".format(totalExpenses)
        displayIncomePrice.text = "R %.2f".format(totalIncome)
        displayBalance.text = "R %.2f".format(balance)

        setupPieChart(totalExpenses, totalIncome)

        if (transactions.isEmpty()) {
            emptyTransactionText.visibility = View.VISIBLE
            transactionList.visibility = View.GONE
        } else {
            emptyTransactionText.visibility = View.GONE
            transactionList.visibility = View.VISIBLE

            transactionList.layoutManager = LinearLayoutManager(requireContext())
            transactionList.adapter = TransactionAdapter(transactions)
        }
    }

    private fun setupPieChart(totalExpenses: Double, totalIncome: Double) {
        val entries = listOf(
            PieEntry(totalExpenses.toFloat(), "Expenses"),
            PieEntry(totalIncome.toFloat(), "Income")
        )

        val dataSet = PieDataSet(entries, "Overview")
        dataSet.colors = listOf(Color.RED, Color.GREEN)
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 16f

        val data = PieData(dataSet)
        expenseVsIncomeChart.data = data
        expenseVsIncomeChart.description.isEnabled = false
        expenseVsIncomeChart.setUsePercentValues(true)
        expenseVsIncomeChart.setEntryLabelColor(Color.BLACK)
        expenseVsIncomeChart.invalidate()
    }

    private fun openFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
