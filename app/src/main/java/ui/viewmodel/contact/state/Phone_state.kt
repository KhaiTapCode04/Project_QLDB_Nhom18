package ui.viewmodel.contact.state

import Phone
import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.contact.sharedPreferences.EmailPreferencesManage
import ui.viewmodel.contact.sharedPreferences.PhonePreferencesManage
import ui.viewmodel.users.UserPreferencesManager
import kotlin.collections.plus

class Phone_state : ViewModel() {
    private val _phone = MutableStateFlow<List<Phone>>(emptyList())
    val phones = _phone.asStateFlow()

    suspend fun get_phone(context: Context) {
        val listFromPref = PhonePreferencesManage(context).getPhoneList()

        if(listFromPref.isEmpty()){
            val user_id = UserPreferencesManager(context).getUserId()
            val phoneList = Contact_service().get_phone(user_id)
            phoneList.forEach {
                PhonePreferencesManage(context).savePhoneList(it)
                addOrUpdatePhone(it)
            }
            _phone.value = phoneList
        } else {
            PhonePreferencesManage(context).getPhoneList().forEach {
                addOrUpdatePhone(it)
            }
        }
    }
    suspend fun reload_phone( context: Context){
        val user_id = UserPreferencesManager(context).getUserId()
        val email = Contact_service().get_phone(user_id)
        email.forEach {
            PhonePreferencesManage(context).savePhoneList(it)
            addOrUpdatePhone(it)
        }
    }

    fun addOrUpdatePhone(newPhone: Phone) {
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