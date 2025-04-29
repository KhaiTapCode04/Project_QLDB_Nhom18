package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel


import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


//data class Data(
//    val id: String = "",
//    val username: String = "",
//    val email: String = "",
//)


import com.example.nhom18_lttbdd_qldb_ngaybc.models.ApiResponsedata
import com.example.nhom18_lttbdd_qldb_ngaybc.models.UserResponse
import com.example.nhom18_lttbdd_qldb_ngaybc.network.ApiService

class LoginViewModel : ViewModel() {
    var username = mutableStateOf("")
    var password = mutableStateOf("")
    var loginError = mutableStateOf<String?>(null)

    private val _userDataList = mutableStateOf<List<UserResponse>>(emptyList())
    val userDataList: State<List<UserResponse>> = _userDataList

    fun login(username: String, password: String, callback: (UserResponse?) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://nettruyen.world/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        apiService.login(username, password).enqueue(object : Callback<ApiResponsedata> {
            override fun onResponse(call: Call<ApiResponsedata>, response: Response<ApiResponsedata>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.isSuccess == true) {
                        _userDataList.value = listOf(apiResponse.data)
                        loginError.value = null
                        callback(apiResponse.data)
                    } else {
                        loginError.value = apiResponse?.reason ?: "Đăng nhập thất bại"
                        callback(null)
                    }
                } else {
                    loginError.value = "Lỗi máy chủ"
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ApiResponsedata>, t: Throwable) {
                loginError.value = "Lỗi kết nối: ${t.localizedMessage}"
                callback(null)
            }
        })
    }


    //    luu thong tin user da dang nhap vao SharedPre
    fun saveUserInfo(user: UserResponse, context: Context) {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("userId", user.id)
            putString("username", user.username)
            putString("email", user.email)
            putString("phone", user.phone)
            apply()
        }
    }

}
