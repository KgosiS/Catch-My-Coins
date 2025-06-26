package com.example.catchmycoins

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class CreateGoalFragment : Fragment() {

    private lateinit var goalNameInput: EditText
    private lateinit var minAmountInput: EditText
    private lateinit var maxAmountInput: EditText
    private lateinit var createButton: Button
    private lateinit var cancelText: TextView
    private lateinit var backButton: ImageView
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_goal, container, false)

        // Initialize shared preferences and DB helper
        sharedPrefs = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        dbHelper = UserDatabaseHelper(requireContext())

        // Bind views
        goalNameInput = view.findViewById(R.id.goalNameEditText)
        minAmountInput = view.findViewById(R.id.minAmountEditText)
        maxAmountInput = view.findViewById(R.id.maxAmountEditText)
        createButton = view.findViewById(R.id.saveGoalButton)
        cancelText = view.findViewById(R.id.cancelText)
        backButton = view.findViewById(R.id.back)

        // Listeners
        createButton.setOnClickListener {
            saveGoal()
        }

        cancelText.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun saveGoal() {
        val name = goalNameInput.text.toString().trim()
        val minText = minAmountInput.text.toString().trim()
        val maxText = maxAmountInput.text.toString().trim()

        if (name.isEmpty() || minText.isEmpty() || maxText.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val min = minText.toDoubleOrNull()
        val max = maxText.toDoubleOrNull()

        if (min == null || max == null || min >= max) {
            Toast.makeText(requireContext(), "Invalid amounts", Toast.LENGTH_SHORT).show()
            return
        }

        val goal = MinMaxGoal(goalName = name, min = min, max = max)
        val success = dbHelper.insertMinMaxGoal(goal)

        if (success) {
            Toast.makeText(requireContext(), "Goal saved!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        } else {
            Toast.makeText(requireContext(), "Failed to save goal", Toast.LENGTH_SHORT).show()
        }
    }
}
