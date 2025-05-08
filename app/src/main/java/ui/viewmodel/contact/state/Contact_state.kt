package ui.viewmodel.contact.state

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.api.ApiService
import data.model.Contact
import data.model.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ui.viewmodel.contact.Contact_service
import ui.viewmodel.contact.sharedPreferences.ConactPreferencesManage
import ui.viewmodel.users.UserPreferencesManager

class Contact_state: ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()
    private val _forceUpdate = MutableStateFlow(false)
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/contacts/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)
    fun blockContact(contactId: Int, context: Context) {
        val userId = UserPreferencesManager(context).getUserId()
        viewModelScope.launch {
            api.blockContact(userId, contactId)
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
    val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups = _groups.asStateFlow()
    fun getGroup() {
        viewModelScope.launch {
            _groups.value = Contact_service().get_group()
        }
    }
    fun getGroupnameById(group_id: Int) : String?{
        return _groups.value.find { it.group_id == group_id }?.group_name
    }
}