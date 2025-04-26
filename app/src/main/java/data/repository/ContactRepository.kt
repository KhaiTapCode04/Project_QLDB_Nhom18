package data.repository


import data.api.ApiService
import data.model.Contact
import data.model.Get_contacts
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import data.model.User
import data.model.Get_user

class ContactRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/contacts/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun Get_contacts(user_id: Int): Contact? {
        return try {
            val response = apiService.getContact(user_id)
            if (response.isSuccess) response.data else null
        } catch (e: Exception) {
            null
        }
    }
}