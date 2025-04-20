package com.example.nhom18_lttbdd_qldb_ngaybc.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class AddContactViewModel : ViewModel() {
    var name = mutableStateOf("")
    var phone = mutableStateOf("")
    var email = mutableStateOf("")
    var birthday = mutableStateOf("")
    var address = mutableStateOf("")
    var note = mutableStateOf("")

    fun clearFields() {
        name.value = ""
        phone.value = ""
        email.value = ""
        birthday.value = ""
        address.value = ""
        note.value = ""
    }

    fun saveContact(
        context: Context,
        mainViewModel: MainViewModel,
        onResult: (Boolean) -> Unit
    ) {
        val contactName = name.value
        val contactPhone = phone.value
        val contactEmail = email.value

        Log.d("SaveContact", "Thông tin lưu: $contactName, $contactPhone, $contactEmail")

        if (contactName.isBlank() || contactPhone.isBlank()) {
            onResult(false)
            return
        }

        mainViewModel.addContact(context, contactName, contactEmail, contactPhone) { success ->
            if (success) {
                clearFields()
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }



}
