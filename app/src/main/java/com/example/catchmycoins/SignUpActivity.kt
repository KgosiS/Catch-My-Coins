package com.example.catchmycoins

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {

    private lateinit var emailInput: TextInputEditText
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signUpButton: Button
  private lateinit var loginText: TextView
    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize views
        emailInput = findViewById(R.id.emailInput)
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        signUpButton = findViewById(R.id.signUpButton)
        loginText = findViewById(R.id.loginText)
        loginText.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        dbHelper = UserDatabaseHelper(this)
        sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        signUpButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            } else if (dbHelper.userExists(email)) {
                Toast.makeText(this, "Email already registered!", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.insertUser(email, username, password)
                if (success) {
                    saveToPreferences(email, username)
                    Toast.makeText(this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveToPreferences(email: String, username: String) {
        with(sharedPrefs.edit()) {
            putString("email", email)
            putString("username", username)
            apply()
        }
    }
}
