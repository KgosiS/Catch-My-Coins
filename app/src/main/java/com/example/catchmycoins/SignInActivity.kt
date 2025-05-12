package com.example.catchmycoins

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        dbHelper = UserDatabaseHelper(this)
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val usernameInput = findViewById<EditText>(R.id.usernameInput) // Rename this in XML too
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.signInButton)
        val notRegistered = findViewById<TextView>(R.id.signUpText)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isValid = dbHelper.validateUser(username, password)

            if (isValid) {
                val userId = dbHelper.getUserIdByUsernameAndPassword(username, password)
                if (userId != -1) {
                    val editor = sharedPreferences.edit()
                    editor.putInt("userId", userId)
                    editor.putString("username", username)
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }

        }

        notRegistered.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
