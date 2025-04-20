package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    val userName = mutableStateOf("")
    val userEmail = mutableStateOf("")

    fun loadUserData(context: Context) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userName.value = sharedPref.getString("username", "") ?: ""
        userEmail.value = sharedPref.getString("email", "") ?: ""
    }

    fun logout(context: Context) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply() // Xóa sạch dữ liệu trong MyPrefs
    }
}

