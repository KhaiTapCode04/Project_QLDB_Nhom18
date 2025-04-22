package data.repository

import data.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import data.model.data
import data.model.ApiResponse

class AuthRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun login(email: String, password: String): data? {
        return try {
            val response = apiService.loginWithForm(email, password)
            if (response.isSuccess) response.data else null
        } catch (e: Exception) {
            null
        }
    }
}