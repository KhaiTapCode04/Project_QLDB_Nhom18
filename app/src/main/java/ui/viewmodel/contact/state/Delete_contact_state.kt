package ui.viewmodel.contact.state

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import data.model.Contact
import data.model.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.contact.sharedPreferences.DeleteConactPreferencesManage
import ui.viewmodel.contact.sharedPreferences.ConactPreferencesManage
import ui.viewmodel.users.UserPreferencesManager

class Delete_contact_state : ViewModel() {



    private val _deleteContacts = MutableStateFlow<List<Contact>>(emptyList())
    val deleteContacts = _deleteContacts.asStateFlow()

    suspend fun select_delete_contacts(context: Context) {
        if(DeleteConactPreferencesManage(context).getDeleteContactList().isEmpty()) {
            val user_id = UserPreferencesManager(context).getUserId()
            val contact = Contact_service().select_delete_contact(user_id)
            contact.forEach {
                DeleteConactPreferencesManage(context).saveOrUpdateDeleteContact(it)
                addOrUpdateDeleteContact(it)
            }
        }
    }
    suspend fun reload_delete_contacts(context: Context) {
        val user_id = UserPreferencesManager(context).getUserId()
        val contact = Contact_service().select_delete_contact(user_id)
        DeleteConactPreferencesManage(context).clearDeleteContacts()
        clearAll()
        contact.forEach {
            DeleteConactPreferencesManage(context).saveOrUpdateDeleteContact(it)
            addOrUpdateDeleteContact(it)
        }
    }
    fun addOrUpdateDeleteContact(newContact: Contact) {
        _deleteContacts.value = _deleteContacts.value
            .filterNot { it.contact_id == newContact.contact_id }
            .plus(newContact)
    }
    fun removeContactById(emailId: Int) {
        _deleteContacts.value = _deleteContacts.value.filterNot { it.contact_id == emailId }
    }

    fun clearAll() {
        _deleteContacts.value = emptyList()
    }
}