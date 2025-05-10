package ui.viewmodel.contact

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.api.ApiService
import data.model.Group
import data.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ui.viewmodel.contact.state.Contact_state
import ui.viewmodel.users.UserPreferencesManager

class EditContactViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _toastMessage = MutableStateFlow("")
    val toastMessage = _toastMessage.asStateFlow()

    // Các lỗi riêng
    val nameError = MutableStateFlow("")
    val phoneError = MutableStateFlow("")
    val emailError = MutableStateFlow("")

    // Danh sách group và nhóm đã chọn
    val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups = _groups.asStateFlow()

    val _selectedGroup = MutableStateFlow<Group?>(null)
    val selectedGroup = _selectedGroup.asStateFlow()



    fun getGroup() {
        viewModelScope.launch {
            _groups.value = Contact_service().get_group()
        }
    }

    fun updateContact(context: Context, contactId: Int, name: String, email: String, phone: String) {
        val userId = UserPreferencesManager(context).getUserId()
        val groupId = _selectedGroup.value?.group_id

        viewModelScope.launch {

            // Reset lỗi cũ
            nameError.value = ""
            phoneError.value = ""
            emailError.value = ""

            var hasError = false

            if (name.isBlank()) {
                nameError.value = "Vui lòng nhập họ và tên"
                hasError = true
            }
            if (phone.isBlank()) {
                phoneError.value = "Vui lòng nhập số điện thoại"
                hasError = true
            } else if (!phone.matches(Regex("^[0-9+]{9,15}$"))) {
                phoneError.value = "Số điện thoại không hợp lệ"
                hasError = true
            }
            if (email.isBlank()) {
                emailError.value = "Vui lòng nhập email"
                hasError = true
            }
//            else if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$.{2,}"))) {
//                emailError.value = "Email không hợp lệ"
//                hasError = true
//            }
            if (groupId == null) {
                _toastMessage.value = "Vui lòng chọn nhóm"
                return@launch
            }

            if (hasError) return@launch

            _isLoading.value = true

            try {
                val resContact = ContactRepository().editContact(contactId, userId, name, groupId)
                val resEmail =ContactRepository().editEmail(contactId, email)
                val resPhone = ContactRepository().editPhone(contactId, phone)
                if (resContact == true && resEmail== true && resPhone== true) {
                    _toastMessage.value = "Cập nhật thành công!"
                    Contact_state().reload_contact(context)
                } else {
                    _toastMessage.value = "Cập nhật thất bại!"
                }

            } catch (e: Exception) {
                _toastMessage.value = "Lỗi hệ thống hoặc kết nối!"
            }

            _isLoading.value = false
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = ""
    }
}
