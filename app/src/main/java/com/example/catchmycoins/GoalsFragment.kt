package com.example.catchmycoins

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GoalsFragment : Fragment() {

    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MinMaxGoalAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var addButton: ImageButton
    private lateinit var backButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget_goals, container, false)

        // Initialize database helper
        dbHelper = UserDatabaseHelper(requireContext())

        // Initialize UI components
        recyclerView = view.findViewById(R.id.goalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        addButton = view.findViewById(R.id.add_button)
        backButton = view.findViewById(R.id.back)

        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Set click listeners
        addButton.setOnClickListener {
            openAddGoalFragment()
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Load and display goals
        loadGoals()

        return view
    }

    private fun loadGoals() {
        val goals = dbHelper.getAllMinMaxGoals()
        adapter = MinMaxGoalAdapter(goals) // Removed totalExpenses param
        recyclerView.adapter = adapter
    }

    private fun openAddGoalFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, CreateGoalFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
