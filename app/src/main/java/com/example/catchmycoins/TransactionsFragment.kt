package com.example.catchmycoins

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionsFragment : Fragment() {

    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText

    private val calendar = Calendar.getInstance()
    private val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_transactions, container, false)

        // Initialize database helper
        dbHelper = UserDatabaseHelper(requireContext())

        // RecyclerView setup
        recyclerView = rootView.findViewById(R.id.transactionList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize date pickers
        startDateEditText = rootView.findViewById(R.id.startDate)
        endDateEditText = rootView.findViewById(R.id.endDate)

        startDateEditText.setOnClickListener { showDatePicker(startDateEditText) }
        endDateEditText.setOnClickListener { showDatePicker(endDateEditText) }

        // Fetch and display all transactions by default
        val transactions = dbHelper.getAllTransactionsByDateRange("1-1-0000", "31-12-9999")
        transactionAdapter = TransactionAdapter(transactions)
        recyclerView.adapter = transactionAdapter

        return rootView
    }

    private fun showDatePicker(targetEditText: EditText) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            targetEditText.setText(displayFormat.format(calendar.time))

            // If both dates are set — filter transactions
            if (startDateEditText.text.isNotEmpty() && endDateEditText.text.isNotEmpty()) {
                filterTransactions()
            }
        }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun filterTransactions() {
        val startDateStr = startDateEditText.text.toString()
        val endDateStr = endDateEditText.text.toString()

        if (startDateStr.isNotEmpty() && endDateStr.isNotEmpty()) {
            val filteredTransactions = dbHelper.getAllTransactionsByDateRange(startDateStr, endDateStr)
            transactionAdapter.updateTransactions(filteredTransactions)
        }
    }

}
