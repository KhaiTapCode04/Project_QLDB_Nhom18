// ApiService.kt
package com.example.nhom18_lttbdd_qldb_ngaybc.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.example.nhom18_lttbdd_qldb_ngaybc.models.ApiResponsedata
import com.example.nhom18_lttbdd_qldb_ngaybc.models.RegisterResponse



interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ApiResponsedata>
}
/*interface ContactApiService {

}*/

interface RegisterApiService {
    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): Call<RegisterResponse>
}




