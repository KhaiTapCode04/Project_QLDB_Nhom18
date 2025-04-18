package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory



import com.example.nhom18_lttbdd_qldb_ngaybc.models.Contact
import com.example.nhom18_lttbdd_qldb_ngaybc.network.ContactApiService


class MainViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ContactApiService::class.java)

    var allContacts = mutableStateOf<List<Contact>>(emptyList())
        private set

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

    // Gọi API lấy danh sách liên lạc
    fun fetchContacts() {
        apiService.getAllContacts().enqueue(object : Callback<List<Contact>> {
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                if (response.isSuccessful) {
                    allContacts.value = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                // TODO: Xử lý lỗi nếu cần
            }
        })
    }

    // Tìm kiếm
    fun onSearchQueryChanged(newQuery: String) {
        searchQuery.value = newQuery
    }

    // Thêm liên lạc mới
    fun addContact(name: String, phone: String, onResult: (Boolean) -> Unit) {
        apiService.addContact(name, phone).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
                if (response.isSuccessful) {
                    fetchContacts() // Làm mới lại danh sách sau khi thêm
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }

    // Thêm email cho liên lạc
    fun addEmail(contactId: String, email: String, onResult: (Boolean) -> Unit) {
        apiService.addEmail(contactId, email).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }

    // Thêm số điện thoại khác cho liên lạc
    fun addPhone(contactId: String, phone: String, onResult: (Boolean) -> Unit) {
        apiService.addPhone(contactId, phone).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}
