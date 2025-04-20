// Contact.kt
package com.example.nhom18_lttbdd_qldb_ngaybc.models

import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("contact_id") val contactId: String,
    @SerializedName("user_id") val userId: String,
    val name: String
)


data class DeleteResponse(
    val success: Boolean,
    val message: String
)