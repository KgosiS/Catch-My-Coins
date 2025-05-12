package com.example.catchmycoins

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddIncomeFragment : Fragment() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var dateInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var startTimeInput: EditText
    private lateinit var endTimeInput: EditText
    private lateinit var uploadImageButton: Button
    private lateinit var createButton: Button
    private lateinit var cancelText: TextView
    private lateinit var imagePreview: ImageView

    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitmap: Bitmap? = null
    private lateinit var dbHelper: UserDatabaseHelper
    private val CAMERA_REQUEST_CODE = 100
    private var photoUri: String? = null // Store the photo URI if taken

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_income, container, false)

        // Initialize UI elements
        titleInput = view.findViewById(R.id.titleInput)
        descriptionInput = view.findViewById(R.id.descriptionInput)
        categorySpinner = view.findViewById(R.id.categorySpinner)
        dateInput = view.findViewById(R.id.dateInput)
        amountInput = view.findViewById(R.id.amountInput)
        startTimeInput = view.findViewById(R.id.startTimeInput)
        endTimeInput = view.findViewById(R.id.endTimeInput)
        uploadImageButton = view.findViewById(R.id.uploadImageButton)
        createButton = view.findViewById(R.id.createButton)
        cancelText = view.findViewById(R.id.cancelText)
        imagePreview = view.findViewById(R.id.imagePreview)

        // Category dropdown setup
        val categories = listOf("Salary", "Freelance", "Bonus", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Date picker dialog
        dateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, day ->
                dateInput.setText("$day/${month + 1}/$year")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        // Start time picker dialog
        startTimeInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                startTimeInput.setText("${String.format("%02d", hourOfDay)}:${String.format("%02d", minute)}")
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePicker.show()
        }

        // End time picker dialog
        endTimeInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                endTimeInput.setText("${String.format("%02d", hourOfDay)}:${String.format("%02d", minute)}")
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePicker.show()
        }

        // Database helper
        dbHelper = UserDatabaseHelper(requireContext())

        // Capture image button
        uploadImageButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
            } else {
                openCamera()
            }
        }

        // Cancel button
        cancelText.setOnClickListener {
            clearFields()
            requireActivity().onBackPressed()
        }

        // Save income button
        createButton.setOnClickListener {
            val title = titleInput.text.toString()
            val description = descriptionInput.text.toString()
            val category = categorySpinner.selectedItem?.toString()
            val date = dateInput.text.toString()
            val startTime = startTimeInput.text.toString()
            val endTime = endTimeInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (title.isEmpty() || description.isEmpty() || category.isNullOrEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || amount == null) {
                Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.insertIncome(title, description, category, date, startTime, endTime, amount, photoUri)
                if (success) {
                    Toast.makeText(requireContext(), "Income Added!", Toast.LENGTH_SHORT).show()
                    clearFields()
                } else {
                    Toast.makeText(requireContext(), "Failed to add income.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    private fun clearFields() {
        titleInput.text.clear()
        descriptionInput.text.clear()
        categorySpinner.setSelection(0)
        dateInput.text.clear()
        startTimeInput.text.clear()
        endTimeInput.text.clear()
        amountInput.text.clear()
        imagePreview.setImageDrawable(null)
        imagePreview.visibility = View.GONE
        imageBitmap = null
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                imagePreview.setImageBitmap(imageBitmap)
                imagePreview.visibility = View.VISIBLE

                val savedPath = saveImageToInternalStorage(imageBitmap)
                if (savedPath != null) {
                    photoUri = savedPath
                    Toast.makeText(requireContext(), "Image saved successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to save image.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String? {
        return try {
            val filename = "IMG_${System.currentTimeMillis()}.jpg"
            val file = File(requireContext().filesDir, filename)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
