package com.example.ui.viewmodel.group

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import ui.viewmodel.users.UserPreferencesManager

data class Group(
    val group_id: Int,
    val user_id: Int,
    val name: String,
    val count: Int,
    val description: String,
    val created_at: String,
    val updated_at: String
)

class GroupViewModel(private val context: Context) : ViewModel() {

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups

    private val sharedPref = context.getSharedPreferences("groups_pref", Context.MODE_PRIVATE)

    fun loadGroupsFromCache() {
        val json = sharedPref.getString("groups", null)
        if (json != null) {
            val type = object : TypeToken<List<Group>>() {}.type
            val list: List<Group> = Gson().fromJson(json, type)
            _groups.value = list
        } else {
            fetchGroups()
        }
    }

    fun fetchGroups() {
        val userId = UserPreferencesManager(context).getUserId()

        viewModelScope.launch {
            try {
                val client = OkHttpClient()
                val formBody = FormBody.Builder()
                    .add("user_id", userId.toString())
                    .build()

                val request = Request.Builder()
                    .url("https://nettruyen.world/get_groups.php")
                    .post(formBody)
                    .build()

                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: ""

                val result = Gson().fromJson(body, GetGroupResponse::class.java)

                if (result.status == "success") {
                    _groups.value = result.groups

                    // Lưu vào SharedPre
                    val jsonSave = Gson().toJson(result.groups)
                    sharedPref.edit().putString("groups", jsonSave).apply()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class GetGroupResponse(
    val status: String,
    val groups: List<Group>
)
