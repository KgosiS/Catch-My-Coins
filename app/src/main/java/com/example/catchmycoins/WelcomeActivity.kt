package com.example.catchmycoins

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        if (!isFirstTime) {
            // If the user has already opened the app before, go directly to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Close WelcomeActivity
            return
        }

        setContentView(R.layout.activity_welcome)

        val getStartedButton: Button = findViewById(R.id.get_started_button)

        getStartedButton.setOnClickListener {
            // Mark that the user is no longer new
            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstTime", false)
            editor.apply()

            // Navigate to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
