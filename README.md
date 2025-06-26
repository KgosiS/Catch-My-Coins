# 📱 CatchMyCoins

**CatchMyCoins** is a personal finance management Android application designed to help users effortlessly track their income, expenses, and financial goals — all within a clean, offline, and user-friendly interface.

---

## 📖 Overview

In today’s fast-paced world, keeping track of personal finances can be overwhelming. **CatchMyCoins** simplifies this process by providing a streamlined app where users can:
- Record both income and expense transactions.
- Filter transactions by date ranges in a **dd/MM/yyyy** format.
- Attach images to expense entries (e.g., receipts).
- Set, track, and monitor financial goals.
- View historical transaction data.
- Keep everything securely stored locally using SQLite.

---

## ✨ Features

### 🔐 User Authentication
- Sign up and log in with a username and password.
- Each user has a private, local data space.

### 💸 Income & Expense Management
- Add income and expenses with:
  - Amount
  - Date (dd/MM/yyyy)
  - Description
  - (Optional) Start time & End time
  - (Optional for expenses) Image attachment
- View combined or individual transaction histories.

### 📅 Date Range Filtering
- Filter all transactions by selecting a start and end date.
- Date picker dialogues for easy date selection.
- Consistent **dd/MM/yyyy** date format across input, storage, and display.

### 🎯 Financial Goal Setting
- Create financial goals by setting:
  - Goal title
  - Target amount
  - Accumulated amount
- View and monitor your savings/spending goals.

### 🎮 Gamification: Earn Badges
To make money management more engaging, **CatchMyCoins** introduces a **badge system** that rewards users for financial achievements.  
Users can earn badges by:
- Logging consistent income entries  
- Meeting or exceeding financial goals  
- Avoiding excessive expenses  
- Maintaining a positive balance over time  

This adds a fun and motivational layer to personal finance!

### 📊 Income Distribution Chart *(planned)*
- Pie chart showing income distribution by category.

### 📶 Offline Storage
- All data saved locally on your device using **SQLite**.
- No internet connection required.

---

## 🛠️ Tech Stack

| Technology  | Purpose                              |
|-------------|--------------------------------------|
| **Kotlin**         | Main programming language             |
| **Android SDK**    | Android application framework         |
| **SQLite**         | Local data storage                     |
| **RecyclerView**   | List and display transaction history   |
| **DatePickerDialog** | Date selection for filtering and adding transactions |
| **SharedPreferences** | Session and goal tracking |

---
