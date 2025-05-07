package ui.viewmodel.contact.state

import android.content.Context
import androidx.lifecycle.ViewModel
import email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.contact.sharedPreferences.EmailPreferencesManage
import ui.viewmodel.users.UserPreferencesManager

class Email_state : ViewModel() {
    private val _emails = MutableStateFlow<List<email>>(emptyList())
    val emails = _emails.asStateFlow()

    fun addOrUpdateEmail(newEmail: email) {
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
        if(EmailPreferencesManage(context).getEmailList().isEmpty()){
            val user_id = UserPreferencesManager(context).getUserId()
            val email = Contact_service().get_email(user_id)
            email.forEach {
                EmailPreferencesManage(context).saveEmailList(it)
                addOrUpdateEmail(it)

            }
        }else{
            EmailPreferencesManage(context).getEmailList().forEach {
                addOrUpdateEmail(it)
            }
        }
    }
}
