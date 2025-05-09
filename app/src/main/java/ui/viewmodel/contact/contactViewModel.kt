package ui.viewmodel.contact

import android.content.Context
import android.util.Log
import android.widget.Toast
import data.repository.ContactRepository
import ui.view.Navigation
import ui.viewmodel.contact.state.AddContact_state
import ui.viewmodel.contact.state.Block_contact_state
import ui.viewmodel.contact.state.Contact_state
import ui.viewmodel.contact.state.Email_state
import ui.viewmodel.contact.state.Phone_state
import ui.viewmodel.users.UserPreferencesManager

class ContactViewModel {

    suspend fun addContact(AddContact_state : AddContact_state, Contact_state:Contact_state,Email_state:Email_state,Phone_state: Phone_state, context: Context) {
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

        if (contact_id != null) {
            Contact_state.reload_contact(context)
            if (email.isNotBlank()) {
                val addEmailSuccess = ContactRepository().addEmail(contact_id, "personal", email)
                if(addEmailSuccess){
                    Toast.makeText(context,"Đã thêm email thành công",Toast.LENGTH_LONG).show()
                    Email_state.reload_email(context)
                }else{
                    Toast.makeText(context,"Đã thêm email thất bại",Toast.LENGTH_LONG).show()

                }
            }
            if(phone.isNotBlank()){
                val addPhoneSuccess = ContactRepository().addPhone(contact_id, "mobile", phone)
                if(addPhoneSuccess){
                    Toast.makeText(context,"Đã thêm số điện thoại thành công",Toast.LENGTH_LONG).show()
                    Phone_state.reload_phone(context)
                }
                else{
                    Toast.makeText(context,"Đã thêm số điện thoại thất bại",Toast.LENGTH_LONG).show()
                }
            }
        }
        AddContact_state._isLoading.value = false
        AddContact_state.clearAllFields()
    }
    suspend fun blockContact( contact_id: Int, Contact_state: Contact_state,Block_contact_state: Block_contact_state, context: Context){
        val user_id = UserPreferencesManager(context).getUserId()
        val block = ContactRepository().blockContact(user_id, contact_id)
        if(block){
            Toast.makeText(context,"Đã chặn thành công",Toast.LENGTH_LONG).show()
            Contact_state.reload_contact(context)
            Block_contact_state.reload_block_contacts(context)
        }else{
            Toast.makeText(context,"Đã chặn thất bại",Toast.LENGTH_LONG).show()
        }
    }
    suspend fun deleteContact(contact_id: Int, Contact_state: Contact_state, context: Context){
        val delete = ContactRepository().deleteContact(contact_id)
        if(delete){
            Toast.makeText(context,"Đã xóa thành công",Toast.LENGTH_LONG).show()
            Contact_state.reload_contact(context)

        }else{
            Toast.makeText(context,"Đã xóa thất bại",Toast.LENGTH_LONG).show()
        }
    }
    suspend fun unBlockContact(contact_id: Int, Block_contact_state: Block_contact_state,Contact_state: Contact_state, context: Context){
        val delete = ContactRepository().unlockContact(contact_id)
        Log.e(contact_id.toString(),contact_id.toString())
        if(delete){
            Toast.makeText(context,"Đã gỡ chặn thành công",Toast.LENGTH_LONG).show()
            Contact_state.reload_contact(context)
            Block_contact_state.reload_block_contacts(context)


        }else{
            Toast.makeText(context,"Đã gỡ chặn thất bại",Toast.LENGTH_LONG).show()
        }
    }



}
