package ui.viewmodel.users

import android.content.Context
import data.model.User
import data.repository.AuthRepository

object LoginService {
    suspend fun login(context: Context, username: String, password: String): User? {
        if (username.isEmpty() || password.isEmpty()) {
            return null
        }

        val check = AuthRepository().login(username, password)
        if (check != null) {
            val userPrefs = UserPreferencesManager(context)
            val profilePicture = check.profile_picture ?: ""
            userPrefs.saveUserInfo(check.id, check.username, check.email, profilePicture)

            // Tạo user với thông tin cơ bản
            val user = User(check.id, check.username, check.email, check.profile_picture)

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
}