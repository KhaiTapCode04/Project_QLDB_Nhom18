package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class ContactItem(
    val name: String,
    val contactId: Int,
    val phone: String,
    val email: String
)

class MainViewModel : ViewModel() {
    val contacts = mutableStateOf<List<ContactItem>>(emptyList())
    val searchQuery = mutableStateOf("")


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/") // Cập nhật nếu baseUrl khác
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    private fun getUserIdFromPrefs(context: Context): String? {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userId", null)
    }

    fun loadContacts(context: Context) {
        val userId = getUserIdFromPrefs(context)
        if (userId == null) {
            Log.e("MainViewModel", "Không tìm thấy userId trong SharedPreferences")
            return
        }

        Log.d("MainViewModel", "Bắt đầu tải danh bạ cho userId: $userId")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getContacts(userId).execute()
                if (response.isSuccessful) {
                    val json = response.body()
                    Log.d("MainViewModel", "Phản hồi getContacts: $json")

                    if (json != null) {
                        val root = JSONObject(json)
                        val isSuccess = root.getBoolean("isSuccess")
                        Log.d("MainViewModel", "isSuccess: $isSuccess")

                        if (isSuccess) {
                            val dataArray = root.getJSONArray("data")
                            val loadedContacts = mutableListOf<ContactItem>()

                            Log.d("MainViewModel", "Tổng số contact nhận được: ${dataArray.length()}")

                            for (i in 0 until dataArray.length()) {
                                val obj = dataArray.getJSONObject(i)
                                val name = obj.getString("name")
                                val contactId = obj.getInt("id")

                                Log.d("MainViewModel", "Đang xử lý contact $i: name=$name, id=$contactId")

                                // Lấy email
                                val emailRes = api.getEmail(contactId).execute()
                                val emailBody = emailRes.body()
                                val email = if (emailRes.isSuccessful) emailBody ?: "trống" else "trống"
                                Log.d("MainViewModel", "Email response: ${emailBody ?: "null"}")

                                // Lấy phone
                                val phoneRes = api.getPhone(contactId).execute()
                                val phoneBody = phoneRes.body()
                                val phone = if (phoneRes.isSuccessful) phoneBody ?: "trống" else "trống"
                                Log.d("MainViewModel", "Phone response: ${phoneBody ?: "null"}")

                                loadedContacts.add(ContactItem(name, contactId, phone, email))
                            }

                            Log.d("MainViewModel", "Hoàn tất tải danh bạ. Tổng cộng: ${loadedContacts.size}")
                            contacts.value = loadedContacts
                        } else {
                            val reason = root.optString("reason")
                            Log.e("MainViewModel", "API trả về lỗi: $reason")
                        }
                    } else {
                        Log.e("MainViewModel", "Phản hồi từ getContacts null")
                    }
                } else {
                    Log.e("MainViewModel", "Lỗi gọi API getContacts: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Lỗi tải danh bạ: ${e.message}", e)
            }
        }
    }


    interface ApiService {
        @FormUrlEncoded
        @POST("contacts/select.php")
        fun getContacts(@Field("user_id") userId: String): Call<String>

        @FormUrlEncoded
        @POST("contacts/select_email.php")
        fun getEmail(@Field("contact_id") contactId: Int): Call<String>

        @FormUrlEncoded
        @POST("contacts/select_phone.php")
        fun getPhone(@Field("contact_id") contactId: Int): Call<String>
    }
}

