package ui.viewmodel.contact

import data.model.Contact
import data.model.group
import data.repository.ContactRepository
import email
import kotlinx.coroutines.flow.StateFlow
import phone

class Contact_service {
    suspend fun get_group(): List<group>{
        return ContactRepository().getGroup()
    }
    suspend fun get_contact(user_id: Int): List<Contact>{
        return ContactRepository().getContacts(user_id)
    }
    suspend fun get_email(user_id: Int): List<email> {
        return ContactRepository().getEmail(user_id)
    }
    suspend fun get_phone(user_id: Int): List<phone> {
        return ContactRepository().getPhone(user_id)
    }
}