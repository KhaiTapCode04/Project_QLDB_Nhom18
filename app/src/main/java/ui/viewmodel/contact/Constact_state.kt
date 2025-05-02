package ui.viewmodel.contact

import androidx.lifecycle.ViewModel
import data.model.Contact
import email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Constact_state: ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun addOrUpdateContact(newContact: Contact) {
        _contacts.value = _contacts.value
            .filterNot { it.contact_id == newContact.contact_id }
            .plus(newContact)
    }

    fun removeEmailById(emailId: Int) {
        _contacts.value = _contacts.value.filterNot { it.contact_id == emailId }
    }

    fun clearAllEmails() {
        _contacts.value = emptyList()
    }
}
