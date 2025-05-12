package com.example.catchmycoins

data class Goal(
   val id: Int =0,
    val userId: Int,
    val title: String,
    val targetAmount: Double,
    val accumulatedAmount: Double,

)
