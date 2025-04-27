package ui.viewmodel.users

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class test : ViewModel() {

    private val _user_id = MutableStateFlow<Int?>(null)
    val user_id = _user_id.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _profile_picture = MutableStateFlow("")
    val profile_picturel = _profile_picture.asStateFlow()

    fun updateUserName(id: Int, name: String, email: String, profile_picture: String) {
        _user_id.value = id
        _userName.value = name
        _email.value = email
        _profile_picture.value = profile_picture
    }
}