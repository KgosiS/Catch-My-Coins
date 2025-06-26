package com.example.catchmycoins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class IncomeBadgesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_income_badges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the database helper
        val dbHelper = UserDatabaseHelper(requireContext())

        // Retrieve the total number of income records
        val incomeCount = dbHelper.getIncomeCount()

        // UI references
        val beginnerIncomeCard = view.findViewById<LinearLayout>(R.id.beginner_income_card)
        val intermediateIncomeCard = view.findViewById<LinearLayout>(R.id.intermediate_income_card)
        val backArrow = view.findViewById<ImageView>(R.id.backArrow)

        // Beginner badge logic (10+ income logs)
        beginnerIncomeCard.isEnabled = incomeCount >= 1000
        beginnerIncomeCard.alpha = if (incomeCount >= 1000) 1f else 0.4f

        // Intermediate badge logic (50+ income logs)
        intermediateIncomeCard.isEnabled = incomeCount >= 5000
        intermediateIncomeCard.alpha = if (incomeCount >= 5000) 1f else 0.4f

        // Back button logic
        backArrow.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
