package data.repository

import android.util.Log
import data.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import data.model.User


class AuthRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun login(email: String, password: String): User? {
        return try {
            val response = apiService.loginWithForm(email, password)
            if (response.isSuccess) response.data else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun update_user(user_id: Int, email: String, phone: String): Boolean{
        return try{
            val reponse = apiService.update_user(user_id, email,phone)
            reponse.isSuccess
        }
        catch (e: Exception){
            false
        }
    }
    suspend fun update_pass(userId: Int, password: String, newPassword: String): Boolean {
        try {
            val response = apiService.update_pass(userId, password, newPassword)
            Log.d("abcdfet", "$response.isSuccess ${response.reason}")
            return response.isSuccess

        } catch (e: Exception) {
            return false
        }
    }
}