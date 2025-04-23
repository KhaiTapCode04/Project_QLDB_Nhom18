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
        val userId = getUserIdFromPrefs(context) ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getContacts(userId).execute()
                if (response.isSuccessful) {
                    val json = response.body() ?: return@launch
                    val root = JSONObject(json)
                    if (root.getBoolean("isSuccess")) {
                        val dataArray = root.getJSONArray("data")
                        val loadedContacts = mutableListOf<ContactItem>()

                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            val name = obj.getString("name")
                            val contactId = obj.getInt("id")

                            // Lấy email
                            val emailRes = api.getEmail(contactId).execute()
                            val email = if (emailRes.isSuccessful) emailRes.body() ?: "trống" else "trống"

                            // Lấy phone
                            val phoneRes = api.getPhone(contactId).execute()
                            val phone = if (phoneRes.isSuccessful) phoneRes.body() ?: "trống" else "trống"

                            loadedContacts.add(ContactItem(name, contactId, phone, email))
                        }

                        contacts.value = loadedContacts
                    }
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Lỗi tải danh bạ: ${e.message}")
            }
        }
    }

    interface ApiService {
        @FormUrlEncoded
        @POST("contact/select.php")
        fun getContacts(@Field("user_id") userId: String): Call<String>

        @FormUrlEncoded
        @POST("contact/select_email.php")
        fun getEmail(@Field("contact_id") contactId: Int): Call<String>

        @FormUrlEncoded
        @POST("contact/select_phone.php")
        fun getPhone(@Field("contact_id") contactId: Int): Call<String>
    }
}

