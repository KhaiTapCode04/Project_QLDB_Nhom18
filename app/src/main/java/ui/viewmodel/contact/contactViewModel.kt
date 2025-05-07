package ui.viewmodel.contact

import android.content.Context
import android.widget.Toast
import data.repository.ContactRepository
import ui.view.Navigation
import ui.viewmodel.contact.state.AddContact_state
import ui.viewmodel.contact.state.Contact_state
import ui.viewmodel.users.UserPreferencesManager

class ContactViewModel {

    suspend fun addContact(AddContact_state : AddContact_state, Contact_state:Contact_state, context: Context) {
        val name = AddContact_state.name.value
        val selectedGroup = AddContact_state.selectedGroup.value
        val email = AddContact_state.email.value
        val phone = AddContact_state.phone.value
        if (name.isBlank() || selectedGroup == null) {
            Toast.makeText(context,"Vui lòng nhập họ tên và chọn nhóm",Toast.LENGTH_LONG).show()
            return
        }
        val currentUserId = UserPreferencesManager(context).getUserId()
        val groupId = selectedGroup.group_id
        AddContact_state._isLoading.value = true
        val contact_id = ContactRepository().addContacts(currentUserId, name, groupId)
        Toast.makeText(context,contact_id.toString(),Toast.LENGTH_LONG).show()
        if (contact_id != null) {
            Navigation().reload_contact(Contact_state,context)
            if (email.isNotBlank()) {
                val addEmailSuccess = ContactRepository().addEmail(contact_id, "personal", email)
                if(addEmailSuccess){
                    Toast.makeText(context,"Đã thêm email thành công",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context,"Đã thêm email thất bại",Toast.LENGTH_LONG).show()

                }
            }
            if(phone.isNotBlank()){
                val addPhoneSuccess = ContactRepository().addPhone(contact_id, "mobile", phone)
                if(addPhoneSuccess){
                    Toast.makeText(context,"Đã thêm số điện thoại thành công",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(context,"Đã thêm số điện thoại thất bại",Toast.LENGTH_LONG).show()
                }
            }
        }
        AddContact_state._isLoading.value = false
        AddContact_state.clearAllFields()
    }
    suspend fun deleteContact(contact_id: Int, Contact_state: Contact_state, context: Context){
        val delete = ContactRepository().deleteContact(contact_id)
        if(delete){
            Toast.makeText(context,"Đã xóa thành công",Toast.LENGTH_LONG).show()
            Navigation().reload_contact(Contact_state,context)

        }else{
            Toast.makeText(context,"Đã xóa thất bại",Toast.LENGTH_LONG).show()

        }
    }


}
