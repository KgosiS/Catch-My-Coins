package com.example.catchmycoins

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "catch_coins_db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create the users table
        val createUserTable = ("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT," +
                "username TEXT," +
                "password TEXT," +
                "expenses REAL DEFAULT 0," +
                "income REAL DEFAULT 0," +
                "budgetGoal REAL DEFAULT 0)")

        // Create the expenses table (updated)
        val createExpenseTable = ("CREATE TABLE expenses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "type TEXT," +
                "date TEXT," +
                "startTime TEXT," +      // New field
                "endTime TEXT," +        // New field
                "amount REAL," +
                "imagePath TEXT)")

        // Create the incomes table
        val createIncomeTable = ("CREATE TABLE income (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "type TEXT," +
                "date TEXT," +
                "startTime TEXT," +  // New field for start time
                "endTime TEXT," +    // New field for end time
                "amount REAL," +
                "imagePath TEXT)")


        // Create goals table
        val createMinMaxGoalsTable = ("CREATE TABLE minmax_goals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "goalName TEXT NOT NULL," +
                "min REAL NOT NULL," +
                "max REAL NOT NULL)")
        // Execute the SQL commands
        db.execSQL(createUserTable)
        db.execSQL(createExpenseTable)
        db.execSQL(createIncomeTable)
        db.execSQL(createMinMaxGoalsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS expenses")
        db.execSQL("DROP TABLE IF EXISTS income")
        db.execSQL("DROP TABLE IF EXISTS minmax_goals")

        onCreate(db)
    }

    // Insert a user
    fun insertUser(email: String, username: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("email", email)
            put("username", username)
            put("password", password)
        }
        val result = db.insert("users", null, values)
        return result != -1L
    }

    // Insert an expense (updated)
    fun insertExpense(
        title: String,
        description: String,
        amount: Double,
        category: String,
        date: String,
        startTime: String,
        endTime: String,
        imagePath: String?
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
            put("type", category)
            put("date", date)
            put("startTime", startTime)
            put("endTime", endTime)
            put("amount", amount)
            put("imagePath", imagePath)
        }
        val result = db.insert("expenses", null, values)
        return result != -1L
    }

    // Insert income
    // Insert income with start and end times
    fun insertIncome(
        title: String,
        description: String,
        type: String,
        date: String,
        startTime: String,
        endTime: String,
        amount: Double,
        imagePath: String?
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
            put("type", type)
            put("date", date)
            put("startTime", startTime)  // Add startTime
            put("endTime", endTime)      // Add endTime
            put("amount", amount)
            put("imagePath", imagePath)
        }
        val result = db.insert("income", null, values)
        return result != -1L
    }


    // Check if a user exists by email
    fun userExists(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun validateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username = ? AND password = ?",
            arrayOf(username, password)
        )
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }

    fun getUserIdByUsernameAndPassword(username: String, password: String): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM users WHERE username = ? AND password = ?",
            arrayOf(username, password)
        )
        val userId = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        } else {
            -1
        }
        cursor.close()
        return userId
    }

    // Fetch all transactions
    fun getTransactions(): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = this.readableDatabase

        val expenseCursor = db.query(
            "expenses",
            arrayOf("amount", "date", "description"),
            null,
            null,
            null,
            null,
            null
        )
        while (expenseCursor.moveToNext()) {
            val amount = expenseCursor.getDouble(expenseCursor.getColumnIndexOrThrow("amount"))
            val date = expenseCursor.getString(expenseCursor.getColumnIndexOrThrow("date"))
            val description =
                expenseCursor.getString(expenseCursor.getColumnIndexOrThrow("description"))
            transactions.add(Transaction(amount, "Expense", date, description))
        }
        expenseCursor.close()

        val incomeCursor = db.query(
            "income",
            arrayOf("amount", "date", "description"),
            null,
            null,
            null,
            null,
            null
        )
        while (incomeCursor.moveToNext()) {
            val amount = incomeCursor.getDouble(incomeCursor.getColumnIndexOrThrow("amount"))
            val date = incomeCursor.getString(incomeCursor.getColumnIndexOrThrow("date"))
            val description =
                incomeCursor.getString(incomeCursor.getColumnIndexOrThrow("description"))
            transactions.add(Transaction(amount, "Income", date, description))
        }
        incomeCursor.close()

        return transactions
    }

    fun getIncomeTransactionsByDateRange(startDate: String, endDate: String): List<Expense> {
        val db = this.readableDatabase
        val query = """
        SELECT title, description, amount, type, date, startTime, endTime, imagePath FROM income
    WHERE date BETWEEN ? AND ?
    """
        val cursor = db.rawQuery(query, arrayOf(startDate, endDate))
        val transactions = mutableListOf<Expense>()

        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"))
                val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val startTime = cursor.getString(cursor.getColumnIndexOrThrow("startTime"))
                val endTime = cursor.getString(cursor.getColumnIndexOrThrow("endTime"))
                val imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"))

                transactions.add(
                    Expense(title, description, amount, type, date, startTime, endTime, imagePath)
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return transactions
    }



    fun getExpensesTransactionsByDateRange(startDate: String, endDate: String): List<Expense> {
        val db = this.readableDatabase
        val query = """
        SELECT title, description, amount, type, date, startTime, endTime, imagePath FROM expenses
    WHERE date BETWEEN ? AND ?
    """
        val cursor = db.rawQuery(query, arrayOf(startDate, endDate))
        val transactions = mutableListOf<Expense>()

        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"))
                val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val startTime = cursor.getString(cursor.getColumnIndexOrThrow("startTime"))
                val endTime = cursor.getString(cursor.getColumnIndexOrThrow("endTime"))
                val imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"))

                transactions.add(
                    Expense(title, description, amount, type, date, startTime, endTime, imagePath)
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return transactions
    }


    fun getAllTransactionsByDateRange(startDate: String, endDate: String): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = readableDatabase

        val query = """
        SELECT amount, type, date, description, startTime, endTime, imagePath 
        FROM expenses 
        WHERE date BETWEEN ? AND ?
        UNION ALL
        SELECT amount, type, date, description, startTime, endTime, imagePath 
        FROM income 
        WHERE date BETWEEN ? AND ?
        ORDER BY date DESC
    """

        val cursor = db.rawQuery(query, arrayOf(startDate, endDate, startDate, endDate))

        if (cursor.moveToFirst()) {
            do {
                val amount = cursor.getDouble(0)
                val type = cursor.getString(1)
                val date = cursor.getString(2)
                val description = cursor.getString(3)
                val startTime = cursor.getString(4)
                val endTime = cursor.getString(5)
                val imagePath = cursor.getString(6)

                transactions.add(
                    Transaction(amount, type, date, description, startTime, endTime, imagePath)
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return transactions
    }
    // Sum total expenses amount
    fun getExpenseCount(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM(amount) FROM expenses", null)
        val total = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return total
    }

    // Sum total income amount
    fun getIncomeCount(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM(amount) FROM income", null)
        val total = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return total
    }

    fun insertMinMaxGoal(goal: MinMaxGoal): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("goalName", goal.goalName)
            put("min", goal.min)
            put("max", goal.max)
        }
        val result = db.insert("minmax_goals", null, values)
        db.close()
        return result != -1L
    }

    fun getAllExpensesTotal(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM(amount) FROM expenses", null)

        val total = if (cursor.moveToFirst()) {
            cursor.getDouble(0)
        } else {
            0.0
        }

        cursor.close()
        db.close()
        return total
    }

    fun getAllMinMaxGoals(): List<MinMaxGoal> {
        val goals = mutableListOf<MinMaxGoal>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM minmax_goals", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("goalName"))
                val min = cursor.getDouble(cursor.getColumnIndexOrThrow("min"))
                val max = cursor.getDouble(cursor.getColumnIndexOrThrow("max"))

                goals.add(MinMaxGoal(id = id, goalName = name, min = min, max = max))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return goals
    }



}

