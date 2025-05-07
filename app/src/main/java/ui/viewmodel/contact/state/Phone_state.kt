package ui.viewmodel.contact.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import phone
import kotlin.collections.plus

class Phone_state : ViewModel() {
    private val _phone = MutableStateFlow<List<phone>>(emptyList())
    val phones = _phone.asStateFlow()

    fun addOrUpdatePhone(newPhone: phone) {
        _phone.value = _phone.value
            .filterNot { it.phone_id == newPhone.phone_id }
            .plus(newPhone)
    }

    fun removePhoneById(phoneId: Int) {
        _phone.value = _phone.value.filterNot { it.phone_id == phoneId }
    }

    fun clearAllPhones() {
        _phone.value = emptyList()
    }
}