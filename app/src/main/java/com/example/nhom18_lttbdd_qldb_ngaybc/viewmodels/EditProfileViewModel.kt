package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

// 1. Interface Retrofit API
interface UserApiService {
    @FormUrlEncoded
    @POST("update_user.php")
    suspend fun updateUser(
        @Field("id") id: Int,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("phone") phone: String
    ): ApiupdateuserResponse
}

// 2. Data class để nhận phản hồi từ server
data class ApiupdateuserResponse(
    val success: Boolean,
    val message: String
)

class EditProfileViewModel : ViewModel() {

    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val phone = mutableStateOf("")

    private fun getUserIdFromPrefs(context: Context): String? {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userId", null)
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(UserApiService::class.java)

    private val _updateResult = MutableStateFlow<ApiupdateuserResponse?>(null)
    val updateResult: StateFlow<ApiupdateuserResponse?> = _updateResult

    fun updateUser(context: Context, username: String, email: String, phone: String) {
        val id = getUserIdFromPrefs(context)?.toIntOrNull()
        viewModelScope.launch {
            try {
                val response = id?.let { api.updateUser(it, username, email, phone) }
                _updateResult.value = response
            } catch (e: Exception) {
                _updateResult.value = ApiupdateuserResponse(false, "Lỗi: ${e.localizedMessage}")
            }
        }
    }
}
