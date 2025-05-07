package ui.viewmodel.contact

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.model.group
import data.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.viewmodel.users.UserPreferencesManager

class AddContactViewModel : ViewModel() {

    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val phone = mutableStateOf("")
    val groups = mutableStateOf<List<group>>(emptyList())
    val selectedGroup = mutableStateOf<group?>(null)

    val addContactResult = mutableStateOf<String?>(null)
    val isLoading = mutableStateOf(false)

    private val repository = ContactRepository()

    fun getGroup() {
        viewModelScope.launch {
            groups.value = Contact_service().get_group()
        }
    }

    fun addContact(context: Context) {

        // Validate Name và Group (bắt buộc)
        if (name.value.isBlank() || selectedGroup.value == null) {
            addContactResult.value = "Vui lòng nhập họ tên và chọn nhóm"
            return
        }

        val currentName = name.value
        val currentUserId = UserPreferencesManager(context).getUserId().toString()
        val currentEmail = email.value
        val currentPhone = phone.value
        val groupId = selectedGroup.value?.group_id?.toIntOrNull() ?: 0


        if (currentUserId == "-1") {
            addContactResult.value = "Không tìm thấy userId trong thiết bị"
            return
        }

        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val resultBuilder = StringBuilder()

            try {
                // 1. Thêm Contact
                val addContactSuccess = repository.addContacts(currentUserId.toInt(), currentName, groupId)

                if (addContactSuccess) {
                    resultBuilder.append("Thêm Contact thành công\n")

                    // Lấy contact ID vừa tạo
                    val contactList = repository.getContacts(currentUserId.toInt())
                    val contactId = contactList.lastOrNull()?.contact_id

                    if (contactId != null) {

                        // 2. Thêm Email (nếu có)
                        if (currentEmail.isNotBlank()) {
                            val addEmailSuccess = repository.addEmail(contactId, "personal", currentEmail)
                            resultBuilder.append(
                                if (addEmailSuccess) "Thêm Email thành công\n" else "Thêm Email thất bại\n"
                            )
                        }

                        // 3. Thêm Phone (nếu có)
                        if (currentPhone.isNotBlank()) {
                            val addPhoneSuccess = repository.addPhone(contactId, "mobile", currentPhone)
                            resultBuilder.append(
                                if (addPhoneSuccess) "Thêm Phone thành công" else "Thêm Phone thất bại"
                            )
                        }

                    } else {
                        resultBuilder.append("Không tìm được Contact ID để thêm Email/Phone")
                    }

                } else {
                    resultBuilder.append("Thêm Contact thất bại")
                }

            } catch (e: Exception) {
                resultBuilder.append("Lỗi kết nối hoặc server")
            }

            addContactResult.value = resultBuilder.toString()
            isLoading.value = false

            // Clear form nếu thêm contact thành công
            if (resultBuilder.contains("Thêm Contact thành công")) {
                clearAllFields()
            }
        }
    }

    fun clearAllFields() {
        name.value = ""
        email.value = ""
        phone.value = ""
        selectedGroup.value = null
    }
}
