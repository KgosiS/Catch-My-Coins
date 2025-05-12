package com.example.catchmycoins

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Check if the user is logged in
        val username = sharedPrefs.getString("username", null)
        if (username == null) {
            // Redirect to sign-in if not logged in
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // Load default fragment (Home)
        if (savedInstanceState == null) {
            openFragment(ExpenseVsIncomeFragment())
        }

        // Set up bottom navigation
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> openFragment(ExpenseVsIncomeFragment())
                R.id.nav_transactions -> openFragment(TransactionsFragment())
                R.id.nav_category -> openFragment(CategoryFragment())
                R.id.nav_goals -> openFragment(GoalsFragment())
                R.id.analysis -> openFragment(TransactionMenuFragment())
                else -> false
            }
            true
        }



    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
