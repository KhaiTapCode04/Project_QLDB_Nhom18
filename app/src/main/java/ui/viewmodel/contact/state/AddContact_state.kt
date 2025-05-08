package ui.viewmodel.contact.state

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.model.Group
import data.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ui.viewmodel.contact.Contact_service

class AddContact_state: ViewModel() {

    val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    val _phone = MutableStateFlow("")
    val phone = _phone.asStateFlow()

    val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups = _groups.asStateFlow()

    val _selectedGroup = MutableStateFlow<Group?>(null)
    val selectedGroup = _selectedGroup.asStateFlow()


    val _addContactResult = MutableStateFlow<String?>(null)
    val addContactResult = _addContactResult.asStateFlow()


    val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()



    fun getGroup() {
        viewModelScope.launch {
            _groups.value = Contact_service().get_group()
        }
    }
    fun clearAllFields() {
        _name.value = ""
        _email.value = ""
        _phone.value = ""
        _selectedGroup.value = null
    }
}