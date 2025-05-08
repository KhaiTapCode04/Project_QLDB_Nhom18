package ui.viewmodel.contact

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.api.ApiService
import data.model.BlockedContact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ui.viewmodel.users.UserPreferencesManager

class BlockedContactViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://nettruyen.world/contacts/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    private val _blockedContacts = MutableStateFlow<List<BlockedContact>>(emptyList())
    val blockedContacts = _blockedContacts.asStateFlow()


    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _toastMessage = MutableStateFlow("")
    val toastMessage = _toastMessage.asStateFlow()


    fun loadBlockedContacts(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = UserPreferencesManager(context).getUserId()
            try {
                val res = api.getBlockedContacts(userId)
                if (res.isSuccess) {
                    _blockedContacts.value = res.data
                }
            } catch (_: Exception) {
            }
            _isLoading.value = false
        }
    }

    fun unblockContact(context: Context, contactId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = UserPreferencesManager(context).getUserId()
            try {
                val res = api.unblockContact(userId, contactId)
                if (res.isSuccess) {
                    _toastMessage.value = "Bỏ chặn thành công"
                    loadBlockedContacts(context)
                } else {
                    _toastMessage.value = "Bỏ chặn thất bại"
                }
            } catch (e: Exception) {
                _toastMessage.value = "Lỗi hệ thống"
            }
            _isLoading.value = false
        }
    }

    fun clearToast() {
        _toastMessage.value = ""
    }
}
