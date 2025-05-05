package data.repository


import android.util.Log
import data.api.ApiService
import data.model.Contact
import data.model.group
import email
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import kotlinx.coroutines.flow.StateFlow
import phone

class ContactRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/contacts/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun getGroup(): List<group>{
        return try{
            val response = apiService.Get_group()
            if (response.isSuccess){
                response.data
            }
            else{
                emptyList()
            }
        }
        catch (e: Exception) {
            emptyList()
        }
    }



    suspend fun getContacts(userId: Int): List<Contact> {
        return try {
            val response = apiService.getContact(userId)
            if (response.isSuccess) {
                response.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addContacts(userId: Int, name: String, group_id: Int): Boolean {
        return try {
            val response = apiService.addContact(userId,name, group_id)
            response.isSuccess
        } catch (e: Exception) {
            false
        }
    }



    suspend fun getEmail(userId: Int): List<email> {
        return try {
            val response = apiService.GetEmail(userId)
            if (response.isSuccess) {
                Log.d("check", response.isSuccess.toString())
                response.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getPhone(userId: Int): List<phone> {
        return try {
            val response = apiService.GetPhone(userId)
            if (response.isSuccess) {
                Log.d("check", response.isSuccess.toString())
                response.data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

}