package ui.viewmodel.users

import data.model.User
import data.repository.AuthRepository

object LoginService {
    suspend fun login(username: String, password: String): User? {
        if (username.isEmpty() || password.isEmpty()) {
            return null
        }

        val check = AuthRepository().login(username, password)
        return if (check != null) {
            User(check.id, check.username,check.email)
        } else {
            null
        }
    }
}