package com.example.catchmycoins


import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class AddCategoryFragment : Fragment() {

    private lateinit var addCategoryExpense: Button
    private lateinit var addCategoryIncome: Button
    private lateinit var titleInput: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelText: TextView
    private lateinit var backButton: ImageView
    private lateinit var categorySpinner: Spinner

    private var selectedType: String = "Income" // default type
    private lateinit var dbHelper: UserDatabaseHelper // replace with your actual DB helper class

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_category, container, false)

        addCategoryExpense = view.findViewById(R.id.addCategoryExpense)
        addCategoryIncome = view.findViewById(R.id.addCategoryIncome)
        titleInput = view.findViewById(R.id.titleInput)
        saveButton = view.findViewById(R.id.saveButton)
        cancelText = view.findViewById(R.id.cancelText)
        backButton = view.findViewById(R.id.back)
        categorySpinner = view.findViewById(R.id.categorySpinner)

        dbHelper = UserDatabaseHelper(requireContext())

        // Set click listeners for income/expense toggle
        addCategoryIncome.setOnClickListener {
            selectedType = "Income"
            addCategoryIncome.isEnabled = false
            addCategoryExpense.isEnabled = true
            addCategoryIncome.setBackgroundColor(resources.getColor(R.color.yellow))
            addCategoryExpense.setBackgroundColor(resources.getColor(R.color.green))
        }

        addCategoryExpense.setOnClickListener {
            selectedType = "Expense"
            addCategoryExpense.isEnabled = false
            addCategoryIncome.isEnabled = true
            addCategoryExpense.setBackgroundColor(resources.getColor(R.color.yellow))
            addCategoryIncome.setBackgroundColor(resources.getColor(R.color.green))
        }

        // Populate Spinner (optional — example values)
        val spinnerItems = listOf("General", "Food", "Transport", "Utilities", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Save to DB
        saveButton.setOnClickListener {
            saveCategoryToDatabase()
        }

        cancelText.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun saveCategoryToDatabase() {
        val title = titleInput.text.toString().trim()
        val spinnerSelection = categorySpinner.selectedItem?.toString() ?: "General"

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("type", selectedType)
            put("categoryGroup", spinnerSelection)
        }

        val result = db.insert("categories", null, values)
        db.close()

        if (result != -1L) {
            Toast.makeText(requireContext(), "Category added!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        } else {
            Toast.makeText(requireContext(), "Failed to add category", Toast.LENGTH_SHORT).show()
        }
    }
}
