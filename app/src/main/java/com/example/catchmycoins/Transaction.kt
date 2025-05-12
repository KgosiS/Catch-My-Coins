package com.example.catchmycoins

data class Transaction(
    val amount: Double,
    val type: String,  // "Income" or "Expense"
    val date: String,
    val description: String,
    val startTime: String? = null,  // Optional start time
    val endTime: String? = null,    // Optional end time
    val imagePath: String? = null
)


