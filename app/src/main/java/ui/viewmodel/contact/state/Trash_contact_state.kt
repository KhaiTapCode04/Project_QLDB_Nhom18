package ui.viewmodel.contact.state

import data.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.content.Context

interface Trash_contact_state {
    val trashedContacts: StateFlow<List<Contact>>
    suspend fun getTrashedContacts(context: Context)
    suspend fun reloadTrashedContacts(context: Context)
}

class TrashContactStateImpl : Trash_contact_state {
    private val _trashedContacts = MutableStateFlow<List<Contact>>(emptyList())
    override val trashedContacts: StateFlow<List<Contact>> = _trashedContacts.asStateFlow()

    override suspend fun getTrashedContacts(context: Context) {
        // Implement logic to fetch trashed contacts from your data source
        // For example, from a Room database or API
        // _trashedContacts.value = repository.getTrashedContacts()
    }

    override suspend fun reloadTrashedContacts(context: Context) {
        // Implement logic to refresh trashed contacts
        // _trashedContacts.value = repository.getTrashedContacts()
    }
}