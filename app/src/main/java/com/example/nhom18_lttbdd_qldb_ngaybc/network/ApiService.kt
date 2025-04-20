// ApiService.kt
package com.example.nhom18_lttbdd_qldb_ngaybc.network

import com.example.nhom18_lttbdd_qldb_ngaybc.models.ApiResponsecontact
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.example.nhom18_lttbdd_qldb_ngaybc.models.ApiResponsedata
import com.example.nhom18_lttbdd_qldb_ngaybc.models.Contact
import com.example.nhom18_lttbdd_qldb_ngaybc.models.RegisterResponse

import retrofit2.http.GET


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

//mainscreen
interface ContactApiService {

    @FormUrlEncoded
    @POST("contacts/select.php")
    fun getAllContacts(
        @Field("userId") userId: String
    ): Call<ApiResponsecontact>
//    Call<ApiResponsecontact>


    @FormUrlEncoded
    @POST("add_email.php")
    fun addEmail(
        @Field("userId") userId: String,
        @Field("id") contactId: String,
        @Field("email") email: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("add_phone.php")
    fun addPhone(
        @Field("userId") userId: String,
        @Field("id") contactId: String,
        @Field("phone") phone: String
    ): Call<Void>

    @FormUrlEncoded
    @POST("select_phone.php")
    fun searchByPhone(
        @Field("userId") userId: String,
        @Field("phone") phone: String
    ): Call<List<Contact>>

    @FormUrlEncoded
    @POST("select_email.php")
    fun searchByEmail(
        @Field("userId") userId: String,
        @Field("email") email: String
    ): Call<List<Contact>>

    @FormUrlEncoded
    @POST("contacts/add.php")
    fun addContact(
        @Field("userId") userId: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String
    ): Call<Void> // hoặc Call<ApiResponse> nếu server trả object JSON

    @FormUrlEncoded
    @POST("contacts/delete.php")
    fun deleteContact(
        @Field("userId") userId: String,
        @Field("id") contactId: Int
    ): Call<Void> // hoặc Call<ApiResponse> nếu server trả object
}



