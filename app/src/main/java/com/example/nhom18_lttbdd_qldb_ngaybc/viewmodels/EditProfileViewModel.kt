package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class EditProfileViewModel : ViewModel() {

    val name = mutableStateOf("")
    val email = mutableStateOf("")

    // Nếu cần load dữ liệu cũ (ví dụ từ SharedPreferences) thì thêm hàm này
    fun loadCurrentUser(name: String, email: String) {
        this.name.value = name
        this.email.value = email
    }
}
