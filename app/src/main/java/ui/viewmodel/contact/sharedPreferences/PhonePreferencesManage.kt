package ui.viewmodel.contact.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import phone

class PhonePreferencesManage(context: Context) {
    companion object {
        private const val PREF_NAME = "ContactPhonePreferences"
        private const val KEY_EMAIL_LIST = "phone_list"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()


    fun savePhoneList(phone: phone) {
        val currentList = getPhoneList().toMutableList()

        val index = currentList.indexOfFirst { it.phone_id == phone.phone_id }

        if (index != -1) {
            currentList[index] = phone
        } else {

            currentList.add(phone)
        }

        val json = gson.toJson(currentList)
        sharedPreferences.edit()
            .putString(KEY_EMAIL_LIST, json)
            .apply()
    }




    fun getPhoneList(): List<phone> {
        val json = sharedPreferences.getString(KEY_EMAIL_LIST, null)
        return if (json != null) {
            val type = object : TypeToken<List<phone>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
    fun clearPhones() {
        sharedPreferences.edit().remove(KEY_EMAIL_LIST).apply()
    }
}