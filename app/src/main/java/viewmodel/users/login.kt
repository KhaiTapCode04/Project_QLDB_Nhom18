package viewmodel.users

import data.model.data
import data.repository.AuthRepository



// Chuyển thành một đối tượng thay vì một lớp để có thể gọi trực tiếp
object LoginService {
    suspend fun login(username: String, password: String): data? {
        if (username.isEmpty() || password.isEmpty()) {
            return null
        }

        val check = AuthRepository().login(username, password)
        return if (check != null) {
            data(check.id, check.username,check.email)
        } else {
            null
        }
    }
}