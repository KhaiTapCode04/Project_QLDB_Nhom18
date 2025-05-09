package data.api

import Get_email
import Get_phone
import data.model.ApiEditContactResponse
import data.model.Get_contacts
import data.model.Get_group
import data.model.Get_user
import data.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Response

interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    suspend fun loginWithForm(@Field("username") email: String, @Field("password") password: String): Get_user

    @FormUrlEncoded
    @POST("add_email.php")
    suspend fun addEmail(
        @Field("contact_id") contact_id: Int,
        @Field("email_type") email_type: String,
        @Field("email_address") email_address: String
    ): Get_email

    @FormUrlEncoded
    @POST("add_phone.php")
    suspend fun addPhone(
        @Field("contact_id") contactId: Int,
        @Field("phone_type") phone_type: String,
        @Field("phone_number") phone_number: String
    ): Get_phone


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


    @FormUrlEncoded
    @POST("add.php")
    suspend fun addContact( @Field("user_id") user_Id: Int,@Field("name") name: String, @Field("group_id") group_id: Int): Get_contacts

    @FormUrlEncoded
    @POST("deleteContact.php")
    suspend fun deleteContact( @Field("contact_id") contact_id: Int): Get_contacts

    @GET("get_group.php")
    suspend fun getGroupApi(): Get_group


    @FormUrlEncoded
    @POST("edit_contact.php")
    suspend fun editContact(
        @Field("contact_id") contactId: Int,
        @Field("user_id") userId: Int,
        @Field("name") name: String,
        @Field("group_id") groupId: Int
    ): Response<ApiEditContactResponse>

    @FormUrlEncoded
    @POST("edit_email.php")
    suspend fun editEmail(
        @Field("contact_id") contactId: Int,
        @Field("email_address") emailAddress: String,
        @Field("email_type") emailType: String
    ): Response<ApiEditContactResponse>

    @FormUrlEncoded
    @POST("edit_phone.php")
    suspend fun editPhone(
        @Field("contact_id") contactId: Int,
        @Field("phone_number") phoneNumber: String,
        @Field("phone_type") phoneType: String
    ): Response<ApiEditContactResponse>

    @FormUrlEncoded
    @POST("block_contact.php")
    suspend fun blockContact(
        @Field("user_id") userId: Int,
        @Field("contact_id") contactId: Int
    ): Get_contacts

    @FormUrlEncoded
    @POST("unblock_contact.php")
    suspend fun unblock_contact(
        @Field("contact_id") contactId: Int
    ): Get_contacts

    @FormUrlEncoded
    @POST("get_blocked_contacts.php")
    suspend fun get_blocked_contacts(
        @Field("user_id") userId: Int
    ): Get_contacts

    @FormUrlEncoded
    @POST("select_delete_contact.php")
    suspend fun select_delete_contact(
        @Field("user_id") userId: Int
    ): Get_contacts
}


