// ApiService.kt
package com.example.nhom18_lttbdd_qldb_ngaybc.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.example.nhom18_lttbdd_qldb_ngaybc.models.ApiResponse
import com.example.nhom18_lttbdd_qldb_ngaybc.models.Contact
import com.example.nhom18_lttbdd_qldb_ngaybc.models.RegisterResponse

import retrofit2.http.GET


interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ApiResponse>
}

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

//mainscreen
interface ContactApiService {
    @GET("select.php")
    fun getAllContacts(): Call<List<Contact>>

    @FormUrlEncoded
    @POST("add.php")
    fun addContact(
        @Field("name") name: String,
        @Field("phone") phone: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("add_email.php")
    fun addEmail(
        @Field("id") contactId: String,
        @Field("email") email: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("add_phone.php")
    fun addPhone(
        @Field("id") contactId: String,
        @Field("phone") phone: String
    ): Call<Void>
}