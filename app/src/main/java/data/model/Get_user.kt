package data.model

import android.graphics.Bitmap

data class Get_user(
    val isSuccess: Boolean,
    val reason: String,
    val data: User
)
data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val profile_picture : String? = null
)