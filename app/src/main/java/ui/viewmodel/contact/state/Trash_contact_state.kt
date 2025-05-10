package ui.viewmodel.contact.state

import data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface Trash_contact_state {
    val trashedContacts: StateFlow<List<Contact>>

}

class TrashContactStateImpl : Trash_contact_state {
    private val _trashedContacts = MutableStateFlow<List<Contact>>(emptyList())
    override val trashedContacts: StateFlow<List<Contact>> = _trashedContacts.asStateFlow()


}