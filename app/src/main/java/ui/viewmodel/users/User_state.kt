package ui.viewmodel.users

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ui.viewmodel.contact.Constact_state
import ui.viewmodel.contact.Email_state
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import ui.viewmodel.contact.EmailPreferencesManage
import ui.viewmodel.contact.ConactPreferencesManage

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
//khai báo hai biến này để sử dụng addorupdate trong contactviewmodel,emailviewmodel
    lateinit var contactViewModel: Constact_state
    lateinit var emailViewModel: Email_state

    // Kết quả cần báo về UI (UI State), biến lưu quyết định
    var navigateToHome by mutableStateOf(false)
        private set
    init {
//        buoc khoi tao chinh sau khi khai bao
        contactViewModel = Constact_state()
        emailViewModel = Email_state()
    }
//viết cái hàm load cho view sử dụng
    fun loadUserData(context: Context) {
        val user = UserPreferencesManager(context)

        if (user.getUserId() != 0) {
            updateUser(
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.getProfilePicture()
            )

            ConactPreferencesManage(context).getContactList().forEach {
                contactViewModel.addOrUpdateContact(it)
            }

            EmailPreferencesManage(context).getEmailList().forEach {
                emailViewModel.addOrUpdateEmail(it)
            }

            // Yêu cầu UI điều hướng
            navigateToHome = true
        }
    }

    fun setNavigatetoAfalse(){
        navigateToHome = false
    }
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
    fun  clearUser(){
        _user_id.value = 0
        _userName.value = ""
        _email.value = ""
        _phone.value = ""
        _profile_picture.value = ""
    }
}