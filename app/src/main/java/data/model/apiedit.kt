package data.model

data class ApiEditContactResponse(
    val isSuccess: Boolean,
    val reason: String
)

data class ApiResponse(
    val isSuccess: Boolean,
    val reason: String
)