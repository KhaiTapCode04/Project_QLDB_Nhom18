package ui.viewmodel.contact.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.model.Contact

class DeleteConactPreferencesManager(private val context: Context) {
    companion object {
        private const val PREF_NAME = "DeleteContactPreferences"
        private const val KEY_Delete_CONTACT_LIST = "delete_contact_list"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveOrUpdateDeleteContact(contact: Contact) {
        val currentList = getDeleteContactList().toMutableList()
        val index = currentList.indexOfFirst { it.contact_id == contact.contact_id }

        if (index != -1) {
            // Đã có contact này ➔ Cập nhật
            currentList[index] = contact
        } else {
            // Chưa có ➔ Thêm mới
            currentList.add(contact)
        }

        val json = gson.toJson(currentList)
        sharedPreferences.edit()
            .putString(KEY_Delete_CONTACT_LIST, json)
            .apply()
    }

    fun getDeleteContactList(): List<Contact> {
        val json = sharedPreferences.getString(KEY_Delete_CONTACT_LIST, null)
        return if (json != null) {
            val type = object : TypeToken<List<Contact>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun clearDeleteContacts() {
        sharedPreferences.edit()
            .remove(KEY_Delete_CONTACT_LIST)
            .apply()
    }
}