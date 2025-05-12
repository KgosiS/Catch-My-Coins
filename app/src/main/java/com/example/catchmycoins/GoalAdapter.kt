package com.example.catchmycoins


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GoalAdapter(private val goalList: List<Goal>) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    class GoalViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val month: TextView = itemView.findViewById(R.id.goalTitle)
        val minGoal: TextView = itemView.findViewById(R.id.amountText)
        val maxGoal: TextView = itemView.findViewById(R.id.amountAccumulated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goals, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goalList[position]
        holder.month.text = "${goal.title}"
        holder.minGoal.text = "Accumulated Amount: R${goal.accumulatedAmount}"
        holder.maxGoal.text = "Goal Target: R${goal.targetAmount}"
    }

    override fun getItemCount(): Int = goalList.size
}
