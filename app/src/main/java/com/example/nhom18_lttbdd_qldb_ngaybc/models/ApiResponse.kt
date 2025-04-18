package com.example.nhom18_lttbdd_qldb_ngaybc.models

data class ApiResponse(
    val isSuccess: Boolean,
    val reason: String,
    val data: Data,
)

data class Data(
    val id: String = "",
    val username: String = "",
    val email: String = "",
)
data class RegisterResponse(
    val isSuccess: Boolean,
    val reason: String
)