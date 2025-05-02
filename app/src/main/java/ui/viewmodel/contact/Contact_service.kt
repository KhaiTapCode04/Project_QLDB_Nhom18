package ui.viewmodel.contact

import data.model.Contact
import data.repository.ContactRepository
import email
import kotlinx.coroutines.flow.StateFlow

class Contact_service {
    suspend fun get_contact(user_id: Int): List<Contact>{
        return ContactRepository().getContacts(user_id)
    }
    suspend fun get_email(user_id: Int): List<email> {
        return ContactRepository().getEmail(user_id)
    }
}