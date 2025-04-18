package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


import com.example.nhom18_lttbdd_qldb_ngaybc.models.RegisterResponse
import com.example.nhom18_lttbdd_qldb_ngaybc.network.RegisterApiService


class RegisterViewModel : ViewModel() {
    var username = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")

    var registerSuccess = mutableStateOf<Boolean?>(null) // true = thành công, false = thất bại
    var registerErrorReason = mutableStateOf<String?>(null) // lỗi gì thì lưu ở đây

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/") // Cần thay bằng domain chính xác của bạn
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(RegisterApiService::class.java)

    fun register() {
        apiService.register(
            username.value,
            email.value,
            password.value,
            confirmPassword.value
        ).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                Log.d("Register", "Dữ liệu gửi đi -> username: ${username.value}, email: ${email.value}, password: ${password.value}, confirmPassword: ${confirmPassword.value}")

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.isSuccess == true) {
                        registerSuccess.value = true
                        registerErrorReason.value = null
                        //ghi log
                        Log.d("Register", "Đăng ký thành công.")
                    } else {
                        registerSuccess.value = false
                        registerErrorReason.value = body?.reason ?: "Unknown error"
                        //ghi log
                        Log.d("Register", "Đăng ký thất bại: ${body?.reason}")
                    }
                } else {
                    registerSuccess.value = false
                    registerErrorReason.value = "Server error"

                    //ghi log
                    Log.d("Register", "Đăng ký thất bại: Lỗi server.")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registerSuccess.value = false
                registerErrorReason.value = "Connection error: ${t.localizedMessage}"

                //ghi log lỗi kết nối
                Log.d("Register", "Đăng ký thất bại: Lỗi kết nối - ${t.localizedMessage}")
            }
        })
    }
}
