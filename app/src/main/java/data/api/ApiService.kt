package data.api

import data.model.Get_contacts
import data.model.Get_user
import data.model.UploadResponse
import data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    suspend fun loginWithForm(@Field("username") email: String, @Field("password") password: String): Get_user

    @POST("select.php")
    suspend fun getContact(@Field("user_id") user_id: Int): Get_contacts

    @Multipart
    @POST("upload_img.php")
    suspend fun uploadProfileImage(
        @Part("user_id") user_id: RequestBody, // Sửa thành @Part với RequestBody
        @Part image: MultipartBody.Part,
        @Part description: MultipartBody.Part
    ): UploadResponse



}
