package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {
    fun isUserLoggedIn(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null)
        return !userId.isNullOrEmpty()
    }
}
