package data.model

import com.google.gson.annotations.SerializedName

data class AddEmailResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("reason") val reason: String
)