package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.nhom18_lttbdd_qldb_ngaybc.models.ApiResponsecontact
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory



import com.example.nhom18_lttbdd_qldb_ngaybc.models.Contact
import com.example.nhom18_lttbdd_qldb_ngaybc.network.ApiService
import com.example.nhom18_lttbdd_qldb_ngaybc.network.ContactApiService
import com.google.gson.Gson
import retrofit2.converter.scalars.ScalarsConverterFactory


class MainViewModel : ViewModel() {

    // Biến lưu danh sách kết quả tìm kiếm server (email/phone)
    var searchResult = mutableStateOf<List<Contact>>(emptyList())
        private set


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ContactApiService::class.java)

    val allContacts = mutableStateOf<List<Contact>>(emptyList())

    var searchQuery = mutableStateOf("")
        private set

    val filteredContacts: List<Contact>
        @Composable get() {
            val query = searchQuery.value.lowercase()
            return allContacts.value
                .filter { it.name.lowercase().contains(query) }
                .sortedBy { it.name }
        }
    val groupedContacts: Map<String, List<Contact>>
        @Composable get() {
            return filteredContacts
                .groupBy { contact -> contact.name.firstOrNull()?.uppercase() ?: "#" }
        }

    fun addContact(context: Context, name: String, email: String, phone: String, onResult: (Boolean) -> Unit) {
        val userId = getUserIdFromPrefs(context)
        if (userId.isNullOrEmpty()) {
            onResult(false)
            Log.d("IDuser:","Khong co userid")
            return
        }

        apiService.addContact(userId, name, email, phone).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchContacts(context)
                    onResult(true)
                } else {
                    Log.e("addContact", "Server error: ${response.code()}")
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("addContact", "Network error: ${t.message}")
                onResult(false)
            }
        })
    }

    /*fun deleteContact(contactId: Int, context: Context, onResult: (Boolean) -> Unit) {

        val userId = getUserIdFromPrefs(context)
        apiService.deleteContact(contactId, userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onResult(true) // Nếu HTTP status 200 thì coi như xóa thành công
                } else {
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }*/


    // Gọi API lấy danh sách liên lạc
    fun fetchContacts(context: Context) {
        Log.e("fetchContacts", "bat dau ham fetch")
        val userId = getUserIdFromPrefs(context)

        if (userId == null) {
            Log.e("fetchContacts", "user id la null")
            return
        }
        Log.e("fetchContacts", "userID: $userId")
        apiService.getAllContacts(userId).enqueue(object : Callback<ApiResponsecontact> {
            override fun onResponse(call: Call<ApiResponsecontact>, response: Response<ApiResponsecontact>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {
                        allContacts.value = body.data // Gán List<Contact> đúng chuẩn
                    } else {
                        Log.e("fetchContacts", "API báo lỗi: ${body?.reason}")
                    }
                } else {
                    Log.e("fetchContacts", "HTTP lỗi: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponsecontact>, t: Throwable) {
                Log.e("fetchContacts", "Lỗi mạng: ${t.localizedMessage}")
            }
        })
    }


    private fun getUserIdFromPrefs(context: Context): String? {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userId", null)
    }



    fun searchContactByPhone(phone: String, context: Context, onResult: (Boolean) -> Unit) {
        val userId = getUserIdFromPrefs(context) ?: return
        apiService.searchByPhone(userId, phone).enqueue(object : Callback<List<Contact>> {
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                if (response.isSuccessful) {
                    searchResult.value = response.body() ?: emptyList()
                    onResult(true)
                } else {
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                onResult(false)
            }
        })
    }
    fun searchContactByEmail(email: String, context: Context, onResult: (Boolean) -> Unit) {
        val userId = getUserIdFromPrefs(context) ?: return
        apiService.searchByEmail(userId, email).enqueue(object : Callback<List<Contact>> {
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                if (response.isSuccessful) {
                    searchResult.value = response.body() ?: emptyList()
                    onResult(true)
                } else {
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                onResult(false)
            }
        })
    }






}
