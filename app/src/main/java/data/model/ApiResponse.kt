package data.model

data class ApiResponse(
    val isSuccess: Boolean,
    val reason: String,
    val data: data
)
data class data(
    val id: String = "",
    val username: String = "",
    val email: String = ""
)