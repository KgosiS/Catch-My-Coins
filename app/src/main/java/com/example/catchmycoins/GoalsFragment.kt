package com.example.catchmycoins

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GoalsFragment : Fragment() {

    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var goalsAdapter: GoalAdapter
    private lateinit var goalsList: RecyclerView
    private lateinit var addButton: ImageButton
    private lateinit var backButton: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget_goals, container, false)

        dbHelper = UserDatabaseHelper(requireContext())
        goalsList = view.findViewById(R.id.listOfGoals)
        addButton = view.findViewById(R.id.add_button)
        backButton = view.findViewById(R.id.back)


        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs",MODE_PRIVATE)
        dbHelper = UserDatabaseHelper(requireContext())
        goalsList.layoutManager = LinearLayoutManager(requireContext())
        val userId =sharedPreferences.getInt("userId",0)
        val goals =dbHelper.getAllGoals(userId)
        goalsAdapter = GoalAdapter(goals)
        goalsList.adapter =goalsAdapter



        addButton.setOnClickListener {
            openAddGoalFragment()
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return view
    }



    private fun openAddGoalFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, CreateGoalFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
