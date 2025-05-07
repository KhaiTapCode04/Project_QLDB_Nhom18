package ui.viewmodel.contact.state

import androidx.lifecycle.ViewModel
import data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Contact_state: ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun addOrUpdateContact(newContact: Contact) {
        _contacts.value = _contacts.value
            .filterNot { it.contact_id == newContact.contact_id }
            .plus(newContact)
    }

    fun removeContactById(emailId: Int) {
        _contacts.value = _contacts.value.filterNot { it.contact_id == emailId }
    }

    fun clearAll() {
        _contacts.value = emptyList()
    }
}