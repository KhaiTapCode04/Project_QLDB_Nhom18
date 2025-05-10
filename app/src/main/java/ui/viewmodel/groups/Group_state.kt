package ui.viewmodel.groups

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import data.model.Group
import data.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ui.viewmodel.contact.Contact_service


class Group_state: ViewModel() {
    private val _group = MutableStateFlow<List<Group>>(emptyList())
    val group = _group.asStateFlow()

    fun addOrUpdateGroup(newGroup: Group) {
        _group.value = _group.value
            .filterNot { it.group_id == newGroup.group_id }
            .plus(newGroup)
    }
    suspend fun get_group(context: Context) {
        val listFromPref = ContactRepository().getGroupApi()
        Log.e("listFromPref",listFromPref.toString())
        if(listFromPref.isEmpty()){
            val GroupList = Contact_service().get_group()
            GroupList.forEach {
                GroupPreferencesManage(context).saveGroupList(it)
                addOrUpdateGroup(it)
            }
            _group.value = GroupList
        } else {
            _group.value = listFromPref
        }
    }
}