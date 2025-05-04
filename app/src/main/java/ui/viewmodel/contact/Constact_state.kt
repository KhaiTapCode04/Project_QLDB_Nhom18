package ui.viewmodel.contact

import android.content.Context
import androidx.lifecycle.ViewModel
import data.model.Contact
import email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ui.viewmodel.users.UserPreferencesManager

class Constact_state : ViewModel() {
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

    suspend fun reload_contact(viewModel: Constact_state, context: Context) {
        val user_id = UserPreferencesManager(context).getUserId()
        val contact = Contact_service().get_contact(user_id)
        contact.forEach {
            ConactPreferencesManage(context).saveOrUpdateContact(it)
            viewModel.addOrUpdateContact(it)
        }
    }
    suspend fun get_contact(context: Context) {
        if(ConactPreferencesManage(context).getContactList().isEmpty()) {
            val user_id = UserPreferencesManager(context).getUserId()
            val contact = Contact_service().get_contact(user_id)
            contact.forEach {
                ConactPreferencesManage(context).saveOrUpdateContact(it)
                addOrUpdateContact(it)
            }
        }
    }
}

