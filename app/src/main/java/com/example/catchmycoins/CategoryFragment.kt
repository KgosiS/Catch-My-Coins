package com.example.catchmycoins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class CategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_category, container, false)

        // Set up onClickListener for Income Category
        rootView.findViewById<LinearLayout>(R.id.incomeCategory).setOnClickListener {
            // Replace with IncomeFragment
            openFragment(AddIncomeFragment())
        }

        // Set up onClickListener for Expense Category
        rootView.findViewById<LinearLayout>(R.id.expenseCategory).setOnClickListener {
            // Replace with ExpenseFragment
            openFragment(AddExpenseFragment())
        }

        return rootView
    }

    // Helper function to replace the current fragment with a new one
    private fun openFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)  // Replace the container with the new fragment
        transaction.addToBackStack(null)  // Optionally add to back stack for navigation
        transaction.commit()
    }
}
