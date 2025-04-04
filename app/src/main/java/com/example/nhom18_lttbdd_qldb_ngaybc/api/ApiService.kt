package com.example.nhom18_lttbdd_qldb_ngaybc.api

import com.example.nhom18_lttbdd_qldb_ngaybc.model.ApiResponse
import com.example.nhom18_lttbdd_qldb_ngaybc.model.Contact
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/Project_QLDB_Nhom18/backend/api/api.php")
    suspend fun checkConnection(@Query("check_connection") check: Boolean = true): Response<ApiResponse>
    @GET("/Project_QLDB_Nhom18/backend/api/api.php")
    suspend fun getAllUsers(): Response<ApiResponse>

    @GET("/Project_QLDB_Nhom18/backend/api/api.php")
    suspend fun getAllContacts(): Response<ApiResponse>

    @POST("/Project_QLDB_Nhom18/backend/api/api.php")
    suspend fun addContact(@Body contact: Contact): Response<ApiResponse>

    companion object {
        private const val BASE_URL = "http://10.0.2.2/"
        // 10.0.2.2 là địa chỉ localhost của máy tính từ máy ảo Android Emulator

        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}