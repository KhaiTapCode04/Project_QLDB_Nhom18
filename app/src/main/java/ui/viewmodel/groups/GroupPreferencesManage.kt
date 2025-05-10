package ui.viewmodel.groups
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.model.Group

class GroupPreferencesManage(context: Context) {

        companion object {
            private const val PREF_NAME = "GroupPreferences"
            private const val KEY_GROUP_LIST = "Group_list"
        }

        private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        private val gson = Gson()


        fun saveGroupList(group: Group) {
            val currentList = getGroupList().toMutableList()

            val index = currentList.indexOfFirst { it.group_id == group.group_id }

            if (index != -1) {
                currentList[index] = group
            } else {
                currentList.add(group)
            }

            val json = gson.toJson(currentList)
            sharedPreferences.edit()
                .putString(KEY_GROUP_LIST, json)
                .apply()
        }




        fun getGroupList(): List<Group> {
            val json = sharedPreferences.getString(KEY_GROUP_LIST, null)
            return if (json != null) {
                val type = object : TypeToken<List<Group>>() {}.type
                gson.fromJson(json, type)
            } else {
                emptyList()
            }
        }
        fun clearGroups() {
            sharedPreferences.edit().remove(KEY_GROUP_LIST).apply()
        }
}