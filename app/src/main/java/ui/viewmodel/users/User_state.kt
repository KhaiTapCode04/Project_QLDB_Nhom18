package ui.viewmodel.users

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class User_state : ViewModel() {

    private val _user_id = MutableStateFlow<Int>(0)
    val user_id = _user_id.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone = _phone.asStateFlow()

    private val _profile_picture = MutableStateFlow("")
    val profile_picturel = _profile_picture.asStateFlow()

    fun updateUser(id: Int, name: String, email: String, phone: String, profile_picture: String) {
        _user_id.value = id
        _userName.value = name
        _email.value = email
        _phone.value = phone
        _profile_picture.value = profile_picture
    }
    fun updatePicture(profile_picture: String){
        _profile_picture.value = profile_picture
    }
}