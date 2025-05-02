package ui.viewmodel.users
//login



import android.content.Context
import android.util.Log
import data.model.User
import data.repository.AuthRepository

object UserService {
    suspend fun login(context: Context, username: String, password: String): User? {
        if (username.isEmpty() || password.isEmpty()) {
            return null
        }
        val check = AuthRepository().login(username, password)
        if (check != null) {
            val userPrefs = UserPreferencesManager(context)
            val profilePicture = check.profile_picture ?: ""
            userPrefs.saveUserInfo(check.id, check.username, check.email, check.phone, profilePicture)

            // Tạo user với thông tin cơ bản
            val user = User(check.id, check.username, check.email,check.phone, check.profile_picture)

            // Tải ảnh đại diện nếu có
            if (check.profile_picture?.isNotEmpty() == true) {
                val localPath = userPrefs.downloadAndSaveProfileImage(check.profile_picture.toString())
                if (localPath != null) {
                    // Cập nhật đường dẫn cục bộ nếu tải thành công
                    return user.copy(profile_picture = localPath)
                }
            }

            // Trả về user bình thường ngay cả khi không có ảnh hoặc tải ảnh thất bại
            return user
        } else {
            return null
        }
    }
    suspend fun update_user(context: Context, user_id: Int, email: String, phone: String): Boolean {
        val check = AuthRepository().update_user(user_id, email, phone)
        return if (check != false) {
            try {
                val userPrefs = UserPreferencesManager(context)
                userPrefs.saveUserEmail(email)
                userPrefs.saveUserPhone(phone)
                true
            }
            catch (e: Exception){
                false
            }
        } else {
            false
        }
    }
    suspend fun update_pass( user_id: Int, password: String, newPassword: String): Boolean {
        Log.d("abcdf", "$user_id $password $newPassword")
        return AuthRepository().update_pass(user_id, password, newPassword)
    }
}