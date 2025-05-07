
data class Get_phone(
    val isSuccess: Boolean,
    val reason: String,
    val data: List<Phone>
)
data class Phone(
    val contact_id: Int,
    val phone_id: Int,
    val phone_number: String,
    val phone_type: String
)