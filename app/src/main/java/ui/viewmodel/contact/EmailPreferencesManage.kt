package ui.viewmodel.contact



import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.model.Contact
import email


class EmailPreferencesManage(context: Context) {
    companion object {
        private const val PREF_NAME = "ContactEmailPreferences"
        private const val KEY_EMAIL_LIST = "email_list"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()


    fun saveEmailList(email: email) {
        val currentList = getEmailList().toMutableList()

        val index = currentList.indexOfFirst { it.email_id == email.email_id }

        if (index != -1) {
            currentList[index] = email
        } else {

            currentList.add(email)
        }

        val json = gson.toJson(currentList)
        sharedPreferences.edit()
            .putString(KEY_EMAIL_LIST, json)
            .apply()
    }




    fun getEmailList(): List<email> {
        val json = sharedPreferences.getString(KEY_EMAIL_LIST, null)
        return if (json != null) {
            val type = object : TypeToken<List<email>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
    fun clearEmails() {
        sharedPreferences.edit().remove(KEY_EMAIL_LIST).apply()
    }
}
