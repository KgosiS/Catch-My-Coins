package com.example.catchmycoins

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class CreateGoalFragment : Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var targetAmountEditText: EditText
    private lateinit var accumulatedAmountEditText: EditText
    private lateinit var createButton: Button
    private lateinit var cancelText: TextView
    private lateinit var backButton: ImageView
     private lateinit var sharedPrefs: SharedPreferences
     private lateinit var dbHelper:  UserDatabaseHelper  // Replace with your actual DBHelper name

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_goal, container, false)
        sharedPrefs = requireActivity().getSharedPreferences("UserPrefs",MODE_PRIVATE)

        titleEditText = view.findViewById(R.id.titleEditText)
        targetAmountEditText = view.findViewById(R.id.targetAmountEditText)
        accumulatedAmountEditText = view.findViewById(R.id.accumulatedAmountEditText)
        createButton = view.findViewById(R.id.createButton)
        cancelText = view.findViewById(R.id.cancelText)
        backButton = view.findViewById(R.id.back)

        dbHelper = UserDatabaseHelper(requireContext()) // Replace with your DBHelper class name

        createButton.setOnClickListener {
            saveGoal()
        }

        cancelText.setOnClickListener {
            // Optional: navigate back or clear input fields
            parentFragmentManager.popBackStack()
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun saveGoal() {
        val title = titleEditText.text.toString().trim()
        val targetAmount = targetAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val accumulatedAmount = accumulatedAmountEditText.text.toString().toDoubleOrNull() ?: 0.0

        if (title.isEmpty() || targetAmount <= 0 || accumulatedAmount < 0) {
            Toast.makeText(requireContext(), "Please enter valid goal data", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = sharedPrefs.getInt("userId",0)
        val goal = Goal(id,userId,title,targetAmount,accumulatedAmount )

        dbHelper.insertGoal(goal)

        Toast.makeText(requireContext(), "Goal saved!", Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }
}
