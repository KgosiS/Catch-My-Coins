package com.example.catchmycoins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment


class TransactionMenuFragment : Fragment() {

    private lateinit var viewAllTransactions: LinearLayout
    private lateinit var viewAnalyseExpenses: LinearLayout
    private lateinit var viewAnalyseIncome: LinearLayout
    private lateinit var checkLimits: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_transaction_menu, container, false)

        // Initialize views
        viewAllTransactions = view.findViewById(R.id.viewAllTransactions)
        viewAnalyseExpenses = view.findViewById(R.id.viewAnalyseExpenses)
        viewAnalyseIncome = view.findViewById(R.id.viewAnalyseIncome)
        checkLimits = view.findViewById(R.id.checkLimits)

        // Set click listeners
        viewAllTransactions.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TransactionsFragment())
                .addToBackStack(null)
                .commit()
        }

        viewAnalyseExpenses.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ExpenseAnalysisFragment())
                .addToBackStack(null)
                .commit()
        }

        viewAnalyseIncome.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, IncomeAnalysisFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
