package com.example.nhom18_lttbdd_qldb_ngaybc.model


data class Contact(
    val id: Int,
    val name: String,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val notes: String? = null,
    val isFavorite: Boolean = false // Thay đổi kiểu dữ liệu cho dễ sử dụng
)
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val profilePicture: String? = null,
    val accountType: String? = null,
    val isActive: Boolean = true
)

data class ApiResponse(
    val status: String,
    val message: String? = null,
    val data: List<User>? = null, // Thay đổi kiểu dữ liệu để chứa danh sách User
    val id: Int? = null

)