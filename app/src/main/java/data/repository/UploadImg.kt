package data.repository

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import data.api.ApiService
import data.model.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UploadImg {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)
    private fun bitmapToFile(context: Context, bitmap: Bitmap, filename: String): File? {
        return try {
            val file = File(context.cacheDir, filename)
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
            val bitmapData = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    suspend fun uploadProfileImage(context: Context,user_id: Int, bitmap: Bitmap): UploadResponse? {
        return try {
            val file = bitmapToFile(context, bitmap, "profile_image.jpg") ?: return null

            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val imagePart = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestFile
            )

            // Tạo phần mô tả
            val description = MultipartBody.Part.createFormData(
                "description",
                "Profile image uploaded from Android"
            )
            val userIdRequestBody = user_id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val response = apiService.uploadProfileImage(userIdRequestBody,imagePart, description)
            file.delete()
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}