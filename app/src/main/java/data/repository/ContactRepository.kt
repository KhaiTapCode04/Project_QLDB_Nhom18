package data.repository

import Email
import Phone
import android.util.Log
import data.api.ApiService
import data.model.Contact
import data.model.Group
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ContactRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/contacts/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun getGroupApi(): List<Group>{
        return try{
            val response = apiService.getGroupApi()
            Log.e("API get group",":response: ${response.isSuccess}")
            if (response.isSuccess){
                Log.e("Data get group:",": ${response.data}")
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

    suspend fun addContacts(userId: Int, name: String, group_id: Int): Int? {
        return try {
            val response = apiService.addContact(userId,name, group_id)
            if(response.isSuccess){
                response.data.firstOrNull()?.contact_id
            }
            else{
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    suspend fun deleteContact(contact_id: Int): Boolean {
        return try {
            val response = apiService.deleteContact(contact_id)
            response.isSuccess
        } catch (e: Exception) {
            false
        }
    }


    suspend fun getEmail(userId: Int): List<Email> {
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

    suspend fun getPhone(userId: Int): List<Phone> {
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
    suspend fun addEmail(contact_id: Int, email_type: String, email_address: String): Boolean {
        return try {
            Log.d("check2", contact_id.toString() + " " + email_type + " " + email_address)
            val response = apiService.addEmail(contact_id, email_type, email_address)
            response.isSuccess
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addPhone(contactId: Int, phone_type: String, phone_number: String): Boolean {
        return try {
            val response = apiService.addPhone(contactId, phone_type, phone_number)
            response.isSuccess
        } catch (e: Exception) {
            false
        }
    }
}