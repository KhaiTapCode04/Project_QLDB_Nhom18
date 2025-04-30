package data.model

data class UploadResponse(
    val isSuccess: Boolean,
    val reason : String = "",
    val imageUrl: String,
    val fileName: String?
)