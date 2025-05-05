package data.api

import Get_email
import Get_phone
import data.model.Get_contacts
import data.model.Get_user
import data.model.UploadResponse
import kotlinx.coroutines.flow.StateFlow
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

    @FormUrlEncoded
    @POST("select.php")
    suspend fun getContact(@Field("user_id") user_id: Int): Get_contacts

    @Multipart
    @POST("upload_img.php")
    suspend fun uploadProfileImage(
        @Part("user_id") user_id: RequestBody,
        @Part image: MultipartBody.Part,
        @Part description: MultipartBody.Part
    ): UploadResponse

    @FormUrlEncoded
    @POST("update_user.php")
    suspend fun update_user(@Field("user_id") user_id: Int, @Field("email") email: String, @Field("phone") phone: String): Get_user


    @FormUrlEncoded
    @POST("update_user.php")
    suspend fun update_pass(@Field("user_id") userId: Int, @Field("password") password: String, @Field("newPassword") newPassword: String): Get_user

    @FormUrlEncoded
    @POST("select_email.php")
    suspend fun GetEmail(@Field("user_id") user_id: Int): Get_email

    @FormUrlEncoded
    @POST("select_phone.php")
    suspend fun GetPhone(@Field("user_id") user_id: Int): Get_phone
}
