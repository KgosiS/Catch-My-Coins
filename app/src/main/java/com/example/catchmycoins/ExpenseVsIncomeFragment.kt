package com.example.catchmycoins




import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
   private lateinit var addIncomeButton: Button
   private lateinit var addExpenseButton: Button
   private lateinit var learnAboutMoney: LinearLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ensure context is available before initializing dbHelper
        dbHelper = UserDatabaseHelper(requireContext())  // This should be fine in onViewCreated

        // Initialize views
        displayExpensePrice = view.findViewById(R.id.displayExpensePrice)
        displayIncomePrice = view.findViewById(R.id.displayIncomePrice)
        displayBalance = view.findViewById(R.id.displayBalance)
        expenseVsIncomeChart = view.findViewById(R.id.expenseVSincome)
        transactionList = view.findViewById(R.id.transactionList)
        emptyTransactionText = view.findViewById(R.id.emptyTransactionText)
        addIncomeButton = view.findViewById(R.id.addIncome)
        addExpenseButton = view.findViewById(R.id.addExpense)
        learnAboutMoney = view.findViewById(R.id.salary)
        learnAboutMoney.setOnClickListener {
            openFragment(LearnAboutMoneyFragment())
        }
        addIncomeButton.setOnClickListener {
            openFragment(AddIncomeFragment())
        }
        addExpenseButton.setOnClickListener {
            openFragment(AddExpenseFragment())
        }



        // Fetch all transactions (expenses and income)
        val transactions = dbHelper.getTransactions()

        // Separate expenses and income
        val expenses = transactions.filter { it.type == "Expense" }
        val income = transactions.filter { it.type == "Income" }

        val totalExpenses = expenses.sumByDouble { it.amount }
        val totalIncome = income.sumByDouble { it.amount }

        displayExpensePrice.text = "R ${totalExpenses}"
        displayIncomePrice.text = "R ${totalIncome}"
        displayBalance.text = "R ${totalIncome - totalExpenses}"

        // Set up the PieChart
        setupPieChart(totalExpenses, totalIncome)

        // Set up RecyclerView with transactions
        if (expenses.isNotEmpty() || income.isNotEmpty()) {
            emptyTransactionText.visibility = View.GONE
            val allTransactions = expenses + income
            val transactionAdapter = TransactionAdapter(allTransactions)
            transactionList.layoutManager = LinearLayoutManager(requireContext())
            transactionList.adapter = transactionAdapter
        } else {
            emptyTransactionText.visibility = View.VISIBLE
        }
    }

    private fun setupPieChart(totalExpenses: Double, totalIncome: Double) {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(totalExpenses.toFloat(), "Expenses"))
        entries.add(PieEntry(totalIncome.toFloat(), "Income"))

        val dataSet = PieDataSet(entries, "Income vs Expense")
        dataSet.setColors(Color.RED, Color.GREEN)
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 18f

        val pieData = PieData(dataSet)
        expenseVsIncomeChart.data = pieData
        expenseVsIncomeChart.invalidate() // refresh chart
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)  // Replace the container with the new fragment
        transaction.addToBackStack(null)  // Optionally add to back stack for navigation
        transaction.commit()
    }
}
