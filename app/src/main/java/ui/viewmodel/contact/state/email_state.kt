package ui.viewmodel.contact.state

import Email
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.model.group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.contact.sharedPreferences.EmailPreferencesManage
import ui.viewmodel.users.UserPreferencesManager

class Email_state : ViewModel() {
    private val _emails = MutableStateFlow<List<Email>>(emptyList())
    val emails = _emails.asStateFlow()

    fun addOrUpdateEmail(newEmail: Email) {
        _emails.value = _emails.value
            .filterNot { it.email_id == newEmail.email_id }
            .plus(newEmail)
    }

    fun removeEmailById(emailId: Int) {
        _emails.value = _emails.value.filterNot { it.email_id == emailId }
    }

    fun clearAllEmails() {
        _emails.value = emptyList()
    }
    suspend fun get_email(context: Context) {
        val listFromPref = EmailPreferencesManage(context).getEmailList()

        if(listFromPref.isEmpty()){
            val user_id = UserPreferencesManager(context).getUserId()
            val emailList = Contact_service().get_email(user_id)
            emailList.forEach {
                EmailPreferencesManage(context).saveEmailList(it)
            }
            _emails.value = emailList
        } else {
            _emails.value = listFromPref
        }
    }

    val _groups = MutableStateFlow<List<group>>(emptyList())
    val groups = _groups.asStateFlow()
    fun getGroup() {
        viewModelScope.launch {
            _groups.value = Contact_service().get_group()
        }
    }
    fun getGroupnameById(group_id: Int) : String?{
        return _groups.value.find { it.group_id == group_id }?.group_name
    }
}
