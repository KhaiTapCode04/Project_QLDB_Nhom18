package com.example.nhom18_lttbdd_qldb_ngaybc.models

data class ApiResponsedata(
    val isSuccess: Boolean,
    val reason: String,
    val data: UserResponse,
)

data class ApiResponsecontact(
    val isSuccess: Boolean,
    val data: List<Contact>,
    val reason: String
)


data class UserResponse(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val phone: String = ""
)
data class RegisterResponse(
    val isSuccess: Boolean,
    val reason: String
)