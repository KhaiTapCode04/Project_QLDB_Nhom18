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
import ui.viewmodel.contact.sharedPreferences.BlockConactPreferencesManage
import ui.viewmodel.contact.sharedPreferences.ConactPreferencesManage
import ui.viewmodel.users.UserPreferencesManager

class Block_contact_state : ViewModel() {



    private val _blockedContacts = MutableStateFlow<List<Contact>>(emptyList())
    val blockedContacts = _blockedContacts.asStateFlow()

    suspend fun get_block_contacts(context: Context) {
        if(BlockConactPreferencesManage(context).getBlockContactList().isEmpty()) {
            val user_id = UserPreferencesManager(context).getUserId()
            val contact = Contact_service().get_blocked_contacts(user_id)
            contact.forEach {
                BlockConactPreferencesManage(context).saveOrUpdateBlockContact(it)
                addOrUpdateBlockContact(it)
            }
        }
    }
    suspend fun reload_block_contacts(context: Context) {
        val user_id = UserPreferencesManager(context).getUserId()
        val contact = Contact_service().get_blocked_contacts(user_id)
        BlockConactPreferencesManage(context).clearBlockContacts()
        clearAll()
        contact.forEach {
            BlockConactPreferencesManage(context).saveOrUpdateBlockContact(it)
            addOrUpdateBlockContact(it)
        }
    }

        fun addOrUpdateBlockContact(newContact: Contact) {
        _blockedContacts.value = _blockedContacts.value
            .filterNot { it.contact_id == newContact.contact_id }
            .plus(newContact)
    }

    fun removeContactById(emailId: Int) {
        _blockedContacts.value = _blockedContacts.value.filterNot { it.contact_id == emailId }
    }

    fun clearAll() {
        _blockedContacts.value = emptyList()
    }


}