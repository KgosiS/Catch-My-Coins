package com.example.catchmycoins

data class Expense(

    val title: String,
    val description: String,
    val amount: Double,
    val type: String,
    val date: String,
    val startTime: String,              // Added start time field
    val endTime: String,                // Added end time field
    val imagePath: String? = null        // Optional image path/URL
)
