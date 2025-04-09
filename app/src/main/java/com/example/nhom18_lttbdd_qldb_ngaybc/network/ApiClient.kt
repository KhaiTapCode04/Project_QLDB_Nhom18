package com.example.nhom18_lttbdd_qldb_ngaybc.network

import android.util.Log
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

/**
 * ApiClient sử dụng Retrofit để giao tiếp với backend PHP
 */
class ApiClient {
    companion object {
        private const val TAG = "ApiClient"

        // URL cơ sở - điều chỉnh nếu cần
        private const val BASE_URL = "http://10.0.2.2/backend/"

        // Logging Interceptor để debug
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val apiService = retrofit.create(ApiService::class.java)

        // In thông tin gọi API để debug
        private fun logApiCall(call: Call<*>) {
            val request = call.request()
            Log.d(TAG, "API Call URL: ${request.url}")
            Log.d(TAG, "API Call Method: ${request.method}")
            Log.d(TAG, "API Call Headers: ${request.headers}")
        }

        fun testConnection(): Boolean {
            try {
                val call = apiService.testConnection()
                logApiCall(call)

                val response = call.execute()
                Log.d(TAG, "Response code: ${response.code()}")

                return response.isSuccessful && response.body()?.status == "success"
            } catch (e: Exception) {
                Log.e(TAG, "Error testing connection: ${e.message}")
                e.printStackTrace()
                return false
            }
        }

        fun testConnectionWithDetails(): String {
            try {
                val call = apiService.testConnection()
                logApiCall(call)

                val response = call.execute()
                Log.d(TAG, "Response code: ${response.code()}")

                return when {
                    response.isSuccessful -> {
                        val apiResponse = response.body()
                        if (apiResponse?.status == "success") {
                            apiResponse.message ?: "Kết nối thành công"
                        } else {
                            "Kết nối thất bại: ${apiResponse?.message ?: "Lỗi không xác định"}"
                        }
                    }
                    else -> {
                        val errorCode = response.code()
                        val errorMessage = response.message()
                        "Kết nối thất bại: HTTP $errorCode - $errorMessage"
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error testing connection: ${e.message}")
                e.printStackTrace()
                return "Lỗi kết nối: ${e.message}"
            }
        }
    }
}

interface ApiService {
    @GET("backend/api/connection/test.php")
    fun testConnection(): Call<ApiResponse>
}

data class ApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?
)