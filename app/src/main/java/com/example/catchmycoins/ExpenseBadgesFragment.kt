package com.example.catchmycoins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class ExpenseBadgesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expense_badges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbHelper = UserDatabaseHelper(requireContext())

        val expenseCount = dbHelper.getExpenseCount()

        val beginnerExpenseCard = view.findViewById<LinearLayout>(R.id.beginner_expense_card)
        val intermediateExpenseCard = view.findViewById<LinearLayout>(R.id.intermediate_expense_card)
        val backArrow = view.findViewById<ImageView>(R.id.backArrow)

        // Show beginner badge if 1000 expenses
        val isBeginnerUnlocked = expenseCount >= 1000
        beginnerExpenseCard.alpha = if (isBeginnerUnlocked) 1f else 0.4f
        beginnerExpenseCard.isEnabled = isBeginnerUnlocked

        // Show intermediate badge if 5000 expenses
        val isIntermediateUnlocked = expenseCount >= 5000
        intermediateExpenseCard.alpha = if (isIntermediateUnlocked) 1f else 0.4f
        intermediateExpenseCard.isEnabled = isIntermediateUnlocked

        // Back arrow functionality
        backArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
