package com.example.catchmycoins

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator

class MinMaxGoalAdapter(
    private val goals: List<MinMaxGoal>
) : RecyclerView.Adapter<MinMaxGoalAdapter.GoalViewHolder>() {

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalName: TextView = itemView.findViewById(R.id.goalName)
        val minText: TextView = itemView.findViewById(R.id.minText)
        val maxText: TextView = itemView.findViewById(R.id.maxText)
        val progressBar: LinearProgressIndicator = itemView.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goals, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]

        holder.goalName.text = goal.goalName
        holder.minText.text = "Min: R ${goal.min}"

        // Simulated accumulated amount (midpoint for now)
        val fakeAccumulated = (goal.min + goal.max) / 2

        if (fakeAccumulated > goal.max) {
            holder.maxText.text = "Max limit exceeded"
            holder.maxText.setTextColor(Color.RED)
        } else {
            holder.maxText.text = "Max: R ${goal.max}"
            holder.maxText.setTextColor(Color.WHITE)
        }

        val progressPercent = ((fakeAccumulated - goal.min) / (goal.max - goal.min) * 100).toInt()
            .coerceIn(0, 100)

        holder.progressBar.progress = progressPercent
    }

    override fun getItemCount(): Int = goals.size
}
