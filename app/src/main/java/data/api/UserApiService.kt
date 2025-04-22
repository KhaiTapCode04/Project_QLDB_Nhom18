package data.api

import data.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    suspend fun loginWithForm(@Field("username") email: String, @Field("password") password: String): ApiResponse
}
