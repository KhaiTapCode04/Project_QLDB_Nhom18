package ui.viewmodel.contact

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ui.view.UserScreen
import ui.viewmodel.users.UserPreferencesManager

class EditContactViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // Khởi tạo Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/contacts/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    fun updateContact(context: Context,contactId: Int ,name: String, groupId: String, email: String, phone: String) {
        val userId = UserPreferencesManager(context).getUserId()
        viewModelScope.launch {

            // Validate
            if (name.isBlank()) {
                _message.value = "Vui lòng nhập họ và tên"
                return@launch
            }

            if (phone.isBlank()) {
                _message.value = "Vui lòng nhập số điện thoại"
                return@launch
            }

            if (!phone.matches(Regex("^[0-9+]{9,15}$"))) {
                _message.value = "Số điện thoại không hợp lệ"
                return@launch
            }

            if (email.isBlank()) {
                _message.value = "Vui lòng nhập email"
                return@launch
            }

            if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$.{2,}"))) {
                _message.value = "Email không hợp lệ"
                return@launch
            }

            if (groupId.isBlank()) {
                _message.value = "Vui lòng nhập ID nhóm"
                return@launch
            }

            _isLoading.value = true
            _message.value = ""

            try {

                val resContact = api.editContact(contactId, userId, name, groupId.toInt())
                val resEmail = api.editEmail(contactId, email, "work")
                val resPhone = api.editPhone(contactId, phone, "mobile")

                if (resContact.isSuccessful && resContact.body()?.isSuccess == true &&
                    resEmail.isSuccessful && resEmail.body()?.isSuccess == true &&
                    resPhone.isSuccessful && resPhone.body()?.isSuccess == true) {

                    _message.value = "Cập nhật thành công!"
                } else {
                    _message.value = "Cập nhật thất bại!"
                }

            } catch (e: Exception) {
                _message.value = "Lỗi hệ thống hoặc kết nối!"
            }

            _isLoading.value = false
        }
    }
}
