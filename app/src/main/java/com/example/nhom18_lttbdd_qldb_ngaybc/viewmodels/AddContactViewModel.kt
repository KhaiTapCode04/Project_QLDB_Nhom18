package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class AddContactViewModel : ViewModel() {

    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val phone = mutableStateOf("")
    val addContactResult = mutableStateOf<String?>(null)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/") //
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    private fun getUserIdFromPrefs(context: Context): String? {
        val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userId", null)
    }

    fun addContact(context: Context) {

        val currentName = name.value
        val currentUserId = getUserIdFromPrefs(context)
        val currentEmail = email.value
        val currentPhone = phone.value

        if (currentUserId == null) {
            addContactResult.value = "Không tìm thấy userId trong thiết bị"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val contactResponse = apiService.addContact(currentName, currentUserId).execute()
                if (contactResponse.isSuccessful) {
                    val contactId = contactResponse.body()?.toIntOrNull()

                    if (contactId != null) {
                        Log.e("AddContact", "Contact ID: $contactId")

                        val emailResponse = apiService.addEmail(contactId, currentEmail).execute()
                        if (emailResponse.isSuccessful) {
                            Log.e("AddContact", "Thêm email thành công")

                            val phoneResponse = apiService.addPhone(contactId, currentPhone).execute()
                            if (phoneResponse.isSuccessful) {
                                Log.e("AddContact", "Thêm phone thành công")
                                addContactResult.value = "Thêm contact thành công!"
                            } else {
                                addContactResult.value = "Thêm phone thất bại"
                            }
                        } else {
                            addContactResult.value = "Thêm email thất bại"
                        }
                    } else {
                        addContactResult.value = "Không lấy được Contact ID"
                    }
                } else {
                    addContactResult.value = "Thêm contact thất bại"
                }
            } catch (e: Exception) {
                Log.e("AddContact", "Lỗi: ${e.localizedMessage}")
                addContactResult.value = "Lỗi kết nối hoặc server"
            }
        }
    }

    interface ApiService {
        @FormUrlEncoded
        @POST("contact/add.php")
        fun addContact(
            @Field("name") name: String,
            @Field("userId") userId: String
        ): retrofit2.Call<String>

        @FormUrlEncoded
        @POST("contact/add_email.php")
        fun addEmail(
            @Field("contact_id") contactId: Int,
            @Field("email") email: String
        ): retrofit2.Call<String>

        @FormUrlEncoded
        @POST("contact/add_phone.php")
        fun addPhone(
            @Field("contact_id") contactId: Int,
            @Field("phone") phone: String
        ): retrofit2.Call<String>
    }
}
