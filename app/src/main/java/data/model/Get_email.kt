
data class Get_email(
    val isSuccess: Boolean,
    val reason: String,
    val data: List<email>
)
data class email(
    val contact_id: Int,
    val email_id: Int,
    val email_address: String,
    val email_type: String
)