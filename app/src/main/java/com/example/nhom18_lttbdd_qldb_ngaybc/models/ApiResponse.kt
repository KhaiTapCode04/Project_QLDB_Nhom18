package com.example.nhom18_lttbdd_qldb_ngaybc.models

data class ApiResponsedata(
    val isSuccess: Boolean,
    val reason: String,
    val data: Data,
)

data class ApiResponsecontact(
    val isSuccess: Boolean,
    val data: List<Contact>,
    val reason: String
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