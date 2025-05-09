package ui.viewmodel.contact

import Email
import Phone
import data.model.Contact
import data.model.Group
import data.model.User
import data.repository.ContactRepository

class Contact_service {

    suspend fun get_group(): List<Group>{
        return ContactRepository().getGroupApi()
    }
    suspend fun get_contact(user_id: Int): List<Contact>{
        return ContactRepository().getContacts(user_id)
    }
    suspend fun get_email(user_id: Int): List<Email> {
        return ContactRepository().getEmail(user_id)
    }
    suspend fun get_phone(user_id: Int): List<Phone> {
        return ContactRepository().getPhone(user_id)
    }
    suspend fun get_blocked_contacts(user_id: Int): List<Contact>{
        return ContactRepository().get_blocked_contacts(user_id)
    }
    suspend fun select_delete_contact(user_id: Int): List<Contact>{
        return ContactRepository().select_delete_contact(user_id)
    }

}