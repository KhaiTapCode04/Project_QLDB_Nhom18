
package ui.viewmodel.contact

import androidx.lifecycle.ViewModel
import email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
}
